package ca.tnoah.frc.scouting.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SerializeEncode {
    //region Encoding

    public static byte[] encodeBase64(String input) {
        return Base64.getEncoder().encode(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeBase64String(String input) {
        return new String(encodeBase64(input), StandardCharsets.UTF_8);
    }

    public static String decodeBase64(byte[] input) {
        return new String(Base64.getDecoder().decode(input), StandardCharsets.UTF_8);
    }

    public static String decodeBase64String(String input) {
        return decodeBase64(input.getBytes(StandardCharsets.UTF_8));
    }

    //endregion Encoding

    //region Serializer

    public static <T> String serializeToJson(T data) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);
    }

    public static <T> T deserializeFromJson(String json, Class<T> classOfT) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json.trim(), classOfT);
    }

    //endregion Serializer

    //region Checksum

    public static String getChecksum(byte[] data) throws IOException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IOException();
        }

        ByteArrayInputStream is = new ByteArrayInputStream(data);

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while ((bytesCount = is.read(byteArray)) != -1)
            digest.update(byteArray, 0, bytesCount);

        byte[] bytes = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes)
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
    }

    public static boolean checkChecksum(byte[] data, String checksum) throws IOException {
        String dataChecksum = getChecksum(data);
        return dataChecksum.trim().equals(checksum.trim());
    }

    //endregion Checksum

    //region Package Data

    public static <T> String packData(T data) throws IOException {
        String serialized = serializeToJson(data);

        byte[] encoded = encodeBase64(serialized);
        String checksum = getChecksum(encoded);

        String encodedString = new String(encoded, StandardCharsets.UTF_8);

        return encodedString + System.lineSeparator() + checksum;
    }

    public static <T> T unpackData(String packedData, Class<T> classOfT) throws UnpackException, IOException {
        String[] lines = packedData.split(System.lineSeparator());

        if (lines.length < 2)
            throw new UnpackException("Not Enough Lines in Packaged Data!");

        byte[] encoded = lines[0].getBytes(StandardCharsets.UTF_8);
        String checksum = lines[1];

        if (!checkChecksum(encoded, checksum))
            throw new UnpackException("Checksums check invalid!");

        String decoded = decodeBase64(encoded);
        return deserializeFromJson(decoded, classOfT);
    }

    //endregion Package Data

    public static class UnpackException extends Exception {

        public UnpackException() {
            super();
        }

        public UnpackException(String message) {
            super(message);
        }

        public UnpackException(String message, Throwable cause) {
            super(message, cause);
        }

        public UnpackException(Throwable cause) {
            super(cause);
        }
    }
}