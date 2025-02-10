package com.example.megacitycab.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageUploadHandler {
    private static final String BASE_UPLOAD_DIRECTORY = "C:/Users/Altha/OneDrive/Desktop/MegaCityCab/Server/megacitycab/src/main/webapp/uploads";

    // Singleton instance
    private static ImageUploadHandler instance;

    // Private constructor to prevent instantiation
    private ImageUploadHandler() {}

    // Static method to get the single instance
    public static ImageUploadHandler getInstance() {
        if (instance == null) {
            synchronized (ImageUploadHandler.class) {
                if (instance == null) {
                    instance = new ImageUploadHandler();
                }
            }
        }
        return instance;
    }

    public static String uploadImage(HttpServletRequest request, String paramName, String folderName) throws IOException, ServletException {
        Part filePart = request.getPart(paramName);

        if (filePart == null || filePart.getSize() == 0) {
            System.out.println("File part is NULL or empty!");
            return null; // No file uploaded
        }

        System.out.println("Uploading file: " + filePart.getSubmittedFileName() + " Size: " + filePart.getSize());

        // Ensure the directory exists
        File uploadDir = new File(BASE_UPLOAD_DIRECTORY, folderName);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Generate unique file name
        String fileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
        String newFileName = System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));

        // Define file path and save
        Path filePath = Path.of(uploadDir.getAbsolutePath(), newFileName);
        Files.copy(filePart.getInputStream(), filePath);

        System.out.println("File saved at: " + filePath);
        
        // Return relative path for serving through web
        return "uploads/" + folderName + "/" + newFileName;
    }

    public static boolean deleteImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            System.out.println("Invalid image path provided for deletion.");
            return false;
        }

        File file = new File(BASE_UPLOAD_DIRECTORY, imagePath.replace("uploads/", "")); // Get the absolute path

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully: " + file.getAbsolutePath());
                return true;
            } else {
                System.out.println("Failed to delete file: " + file.getAbsolutePath());
                return false;
            }
        } else {
            System.out.println("File not found: " + file.getAbsolutePath());
            return false;
        }
    }

}
