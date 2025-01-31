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
        String headerBase64 = base64Encode(headerJson);

        // 2️⃣ Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("sub", email);
        payload.put("iat", nowMillis); // Issued At
        payload.put("exp", expMillis); // Expiration Time
        String payloadJson = mapToJson(payload);
        String payloadBase64 = base64Encode(payloadJson);

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
}
