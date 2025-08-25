package com.chromeExtensions.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public class Util {

    /**
     * Converts a hex string (e.g., "FFD8FF") into a byte array.
     */
    public static byte[] hexStringToByteArray(String hexString) {
        if (hexString == null || hexString.isEmpty()) {
            return new byte[0];
        }
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Reads the first 'length' bytes from a MultipartFile.
     * Throws RuntimeException if reading fails.
     */
    public static byte[] readFileToByteArrayByLength(MultipartFile file, int length) {
        if (file == null || length <= 0) {
            return new byte[0];
        }
        // inputStream is closed automatically by try-with-resources
        try (InputStream inputStream = file.getInputStream()) {
            byte[] fileBytes = new byte[length];
            int bytesRead = inputStream.read(fileBytes, 0, length);
            return fileBytes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file bytes", e);
        }
    }
}
