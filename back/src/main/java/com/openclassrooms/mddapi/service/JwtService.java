package com.openclassrooms.mddapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private static final String SECRET_KEY = "maSuperCleSecreteTresLongue"; // Clé secrète pour signer le token


    // ✅ Générer un token JWT
    public static String generateToken(Long id, String email ) {
        long expirationTimeMillis = 1000 * 60 * 60 * 10; // 10 hours of validity
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expirationTimeMillis;

        // 1️⃣ Header
        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String headerBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

        // 2️⃣ Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("sub", email);
        payload.put("iat", nowMillis); // Issued At
        payload.put("exp", expMillis); // Expiration Time

        String payloadJson = String.format("{\"id\":%d,\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}", id, email, nowMillis, expMillis);
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        // 3️⃣ Signature
        String signature = sign(headerBase64 + "." + payloadBase64, SECRET_KEY);

        // 🏆 Token final
        return headerBase64 + "." + payloadBase64 + "." + signature;
    }

    // ✅ Encodage Base64
    private static String base64Encode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ HMAC-SHA256 pour signer le JWT
    private static String sign(String data, String secretKey) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64Encode(new String(mac.doFinal(data.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la signature du token", e);
        }
    }

    // ✅ Convertir une Map en JSON (simplifié, sans dépendance)
    private static String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\",");
            } else {
                json.append(entry.getValue()).append(",");
            }
        }
        return json.substring(0, json.length() - 1) + "}";
    }

    private static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        json = json.replace("{", "").replace("}", "");
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            map.put(keyValue[0].replace("\"", "").trim(), keyValue[1].replace("\"", "").trim());
        }
        return map;
    }

    public static String extractEmail(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> payload = jsonToMap(payloadJson);

            return (String) payload.get("sub"); // "sub" correspond à l'email stocké dans le JWT
        } catch (Exception e) {
            return null;
        }
    }
    public Long extractUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> payload = jsonToMap(payloadJson);

            // Extraire l'ID utilisateur stocké dans le token
            return Long.parseLong(payload.get("id").toString());
        } catch (Exception e) {
            return null;
        }
    }
    public String getUsernameFromToken(String token) {
        try {
            // Étape 1 : Vérifier si le token est null ou mal formé
            if (token == null || !token.contains(".")) {

                return null;
            }

            // Étape 2 : Décoder le payload du token
            String[] tokenParts = token.split("\\.");
            if (tokenParts.length < 2) {

                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));

            // Étape 3 : Convertir le JSON du payload en une Map
            Map<String, Object> payloadMap = jsonToMap(payload);

            // Étape 4 : Récupérer l'ID depuis la Map
            String userId = (String) payloadMap.get("id");

            return userId;
        } catch (Exception e) {
            return null;// Return null if any decoding or parsing error occurs.
        }
    }

    private Map<String, Object> parsePayload(String payloadJson) {
        Map<String, Object> payload = new HashMap<>();
        payloadJson = payloadJson.replace("{", "").replace("}", ""); // Remove curly braces from the JSON string.
        String[] pairs = payloadJson.split(",");// Split the string into key-value pairs.
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");// Split each pair into key and value.
            payload.put(keyValue[0].trim(), keyValue[1].trim());// Add the pair to the map.
        }
        return payload;
    }
    public boolean validateToken(String token, String username) {
        try {
            String[] parts = token.split("\\.");
            System.out.println(" Nombre de parties après split: " + parts.length);

            if (parts.length != 3) {
                System.out.println("❌ ERREUR: Token mal formé !");
                return false;
            }

            // Vérification de la signature
            String expectedSignature = sign(parts[0] + "." + parts[1], SECRET_KEY);
            System.out.println(" Expected Signature: " + expectedSignature);
            System.out.println("Token Signature: " + parts[2]);

            if (!expectedSignature.equals(parts[2])) {
                System.out.println(" JWT signature mismatch !");
                return false;
            }

            // Décoder et analyser le payload
            System.out.println(" Encodage brut du payload (avant décodage): " + parts[1]);

            try {
                String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
                System.out.println("✅ Payload décodé: " + payloadJson);
            } catch (Exception e) {
                System.out.println(" Erreur de décodage du payload : " + e.getMessage());
            }


            return true;
        } catch (Exception exception) {
            System.out.println(" Token validation failed: " + exception.getMessage());
            return false;
        }
    }
    private String sign(String data) {
        try {
            // Initialize the HMAC-SHA256 algorithm with the secret key.
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            // Generate the signature by hashing the data.
            return Base64.getUrlEncoder().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            // Throw a runtime exception if the signing process fails.
            throw new RuntimeException("Erreur lors de la signature du token JWT", exception);
        }
    }
}
