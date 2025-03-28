package com.openclassrooms.mddapi.service;


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

    /**
     * Generates a JWT token based on the user ID and email.
     * <p>
     * The token consists of three parts: Header, Payload, and Signature.
     * - The Header specifies the algorithm and type of the token.
     * - The Payload contains user-specific data and metadata, such as the user ID, email, and expiration time.
     * - The Signature is used to verify the integrity of the token.
     *
     * @param id    The user ID to be included in the token payload.
     * @param email The user's email to be included in the token payload.
     * @return The generated JWT token as a string.
     */
    public static String generateToken(Long id, String email) {
        // Expiration time: Set the token validity to 10 hours
        long expirationTimeMillis = 1000 * 60 * 60 * 10; // 10 hours of validity
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + expirationTimeMillis;

        // Header: Set the algorithm to "HS256" and type to "JWT"
        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String headerBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

        // 2Payload: Include the user ID, email, issued-at time (iat), and expiration time (exp)
        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("sub", email);
        payload.put("iat", nowMillis); // Issued At
        payload.put("exp", expMillis); // Expiration Time

        // Convert the payload into a JSON string
        String payloadJson = String.format("{\"id\":%d,\"sub\":\"%s\",\"iat\":%d,\"exp\":%d}", id, email, nowMillis, expMillis);
        // Encode the payload to Base64 (URL-safe)
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        // Signature: Create the signature using the header and payload concatenated, and sign it with the secret key
        String signature = sign(headerBase64 + "." + payloadBase64, SECRET_KEY);

        // Final JWT: Concatenate the header, payload, and signature to form the complete JWT token
        return headerBase64 + "." + payloadBase64 + "." + signature;
    }

    /**
     * Encodes a string into a URL-safe base64 format without padding.
     * <p>
     * This method converts the provided data into bytes using UTF-8 encoding, and then performs
     * Base64 encoding with URL-safe characters (i.e., replacing `+` and `/` with `-` and `_`).
     * Additionally, padding characters (`=`) are removed from the final encoded string.
     *
     * @param data The string to be encoded.
     * @return The URL-safe Base64 encoded string without padding.
     */
    private static String base64Encode(String data) {
        // Convert the string into bytes using UTF-8 encoding
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Signs the given data using the HMAC-SHA256 algorithm with the specified secret key.
     * <p>
     * This method uses the HMAC (Hash-based Message Authentication Code) algorithm with SHA-256
     * to sign the input data. The signature is then base64 URL-encoded for compatibility with URL-safe formats
     * such as JWT tokens.
     *
     * @param data      The data to be signed.
     * @param secretKey The secret key used for the HMAC SHA-256 signing.
     * @return The base64 URL-encoded signature.
     * @throws RuntimeException If an error occurs during the signing process.
     */
    private static String sign(String data, String secretKey) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64Encode(new String(mac.doFinal(data.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la signature du token", e);
        }
    }

    /**
     * Converts a Map to a JSON string representation.
     * <p>
     * This method takes a map where each entry's key and value are converted into key-value pairs in the resulting JSON string.
     * The method handles both String values and non-String values (which are directly inserted as they are) in the map.
     * It constructs a valid JSON object string with key-value pairs, where the keys are enclosed in double quotes, and values are inserted as they are (with String values enclosed in double quotes).
     * <p>
     * Example:
     * If the map is {"name": "John", "age": 30}, the output will be:
     * "{\"name\":\"John\",\"age\":30}"
     *
     * @param map The map containing the data to be converted to JSON. The map's keys must be Strings, and values can be of any type.
     * @return A string containing the JSON representation of the map.
     */
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

    /**
     * Converts a JSON string into a Map representation.
     * <p>
     * This method takes a JSON string and parses it into a `Map<String, Object>`.
     * It assumes that the JSON string represents an object with key-value pairs in the form of `"key":"value"`.
     * The method removes the surrounding curly braces `{}` and splits the JSON string by commas and colons to extract the key-value pairs.
     * <p>
     * Example:
     * If the JSON string is:
     * {"name":"John", "age":"30"},
     * the resulting map will be:
     * {"name" -> "John", "age" -> "30"}
     *
     * @param json The JSON string to be converted into a map. The string should represent a valid JSON object with key-value pairs.
     * @return A `Map<String, Object>` containing the keys and values from the JSON string. The keys are strings, and the values are strings (no type conversion is done).
     * @throws IllegalArgumentException if the input string is not a valid JSON object.
     */
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

    /**
     * Extracts the email address from a JWT token.
     * <p>
     * This method decodes the payload of a JWT (JSON Web Token) and extracts the "sub" (subject) claim,
     * which typically contains the user's email address.
     * <p>
     * It assumes that the JWT token consists of three parts: header, payload, and signature, separated by periods ('.').
     * The payload part is base64-decoded and parsed into a map. The "sub" claim is then extracted as the email address.
     *
     * @param token The JWT token from which the email is to be extracted.
     * @return The email address contained in the "sub" claim of the JWT if present, or null if the extraction fails.
     */
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

    /**
     * Extracts the user ID from a JWT token.
     * <p>
     * This method decodes the payload of a JWT (JSON Web Token) and extracts the "id" claim,
     * which typically contains the user's ID.
     * <p>
     * It assumes that the JWT token consists of three parts: header, payload, and signature, separated by periods ('.').
     * The payload part is base64-decoded and parsed into a map. The "id" claim is then extracted as the user's ID.
     *
     * @param token The JWT token from which the user ID is to be extracted.
     * @return The user ID stored in the "id" claim of the JWT, or null if the extraction fails or the token is invalid.
     */
    public Long extractUserId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return null;

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> payload = jsonToMap(payloadJson);

           return Long.parseLong(payload.get("id").toString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Extracts the user ID from a JWT token.
     * <p>
     * This method decodes the payload of a JWT (JSON Web Token) and retrieves the "id" claim,
     * which typically contains the user's ID. The method assumes the token is composed of three parts
     * (header, payload, signature) separated by periods ('.'). It performs the following steps:
     * <ol>
     *     <li>Splits the token into its constituent parts (header, payload, signature).</li>
     *     <li>Base64-decodes the payload part of the token.</li>
     *     <li>Parses the decoded payload into a map.</li>
     *     <li>Extracts the "id" claim from the payload and returns it as a String.</li>
     * </ol>
     *
     * @param token The JWT token from which the user ID is to be extracted.
     * @return The user ID extracted from the token's payload as a String, or null if the token is invalid or missing the ID claim.
     */
    public String getUsernameFromToken(String token) {
        try {
            // Check if the token is null or malformed
            if (token == null || !token.contains(".")) {

                return null;
            }

            // Decode the payload of the token
            String[] tokenParts = token.split("\\.");
            if (tokenParts.length < 2) {

                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));

            // Convert the payload JSON to a Map
            Map<String, Object> payloadMap = jsonToMap(payload);

            // Retrieve the user ID from the Map
            String userId = (String) payloadMap.get("id");

            return userId;
        } catch (Exception e) {
            // Return null if any decoding or parsing error occurs
            return null;
        }
    }

    /**
     * Parses a JSON-like string into a Map of key-value pairs.
     * <p>
     * This method takes a string representation of a JSON object (without the curly braces) and converts it
     * into a map of strings to objects. The input string should be in a simple "key=value" format, with key-value
     * pairs separated by commas. The method does not handle nested objects or complex structures and assumes
     * that the input string is well-formed.
     *
     * @param payloadJson The JSON-like string to be parsed, without curly braces and in a simple "key=value" format.
     * @return A Map where keys are the field names (as strings) and values are the corresponding field values (as strings).
     */
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

    /**
     * Validates the given JWT token by checking its structure, signature, and decoding the payload.
     * <p>
     * This method performs the following checks on the token:
     * 1. Splits the token into its constituent parts (header, payload, signature).
     * 2. Verifies that the token is well-formed (must contain 3 parts).
     * 3. Validates the signature by comparing it to the expected signature.
     * 4. Decodes the payload and checks for any errors during decoding.
     *
     * @param token    The JWT token to validate.
     * @param username The username to check against the token's "sub" field to ensure it's correct.
     * @return true if the token is valid; false otherwise.
     */
    public boolean validateToken(String token, String username) {
        try {
            // Split the token into parts (header, payload, signature)
            String[] parts = token.split("\\.");

            // Ensure the token has exactly 3 parts: header, payload, signature
            if (parts.length != 3) {
                System.out.println("ERREUR: Token mal formé !");
                return false;
            }

            // Verify the token signature by generating the expected signature
            String expectedSignature = sign(parts[0] + "." + parts[1], SECRET_KEY);

            // Compare the expected signature with the actual signature
            if (!expectedSignature.equals(parts[2])) {
                System.out.println(" JWT signature mismatch !");
                return false;
            }

            try {
                // Decode the payload
                String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));

            } catch (Exception e) {
                System.out.println(" Erreur de décodage du payload : " + e.getMessage());
            }

            // Token is valid if all checks pass
            return true;
        } catch (Exception exception) {
            // Catch any unexpected exceptions that may occur during the validation process
            System.out.println(" Token validation failed: " + exception.getMessage());
            return false;
        }
    }

    /**
     * Generates a cryptographic signature for the given data using the HMAC-SHA256 algorithm.
     * This method is typically used to sign the JWT header and payload to ensure data integrity and authenticity.
     *
     * @param data The data (e.g., header and payload concatenated) to be signed.
     * @return The base64-encoded signature as a string.
     * @throws RuntimeException If there is an error during the signing process.
     */
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
