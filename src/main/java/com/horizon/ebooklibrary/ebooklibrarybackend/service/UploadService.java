package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;

/**
 * Service responsible for handling file uploads.
 * It saves files to the local filesystem and returns their public URL.
 */
@SuppressWarnings("unused")
@Service
public class UploadService {

    // Base folder where uploaded files will be stored (defined in application.properties)
    @Value("${upload.dir}")
    private String uploadDir;

    // Base URL prefix for serving files
    @Value("${upload.url-prefix}")
    private String urlPrefix;

    /**
     * Saves a PDF file to the upload directory.
     * @param file The uploaded MultipartFile
     * @return the publicly accessible URL to the upload file
     * @throws IOException if saving fails
     */
    public String savePdf(MultipartFile file) throws IOException {
        // Ensure uploads folder exists
        Path uploadPath = Paths.get("").toAbsolutePath().resolve(uploadDir);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Use the original file name
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are supported.");
        }

        // Save the file to disk
        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath.toFile());

        // Return public URL to access the file
        return urlPrefix + filename;
    }

    public String saveImage(MultipartFile file) throws IOException {
        // Ensure uploads folder exists
        Path uploadPath = Paths.get("").toAbsolutePath().resolve(uploadDir);
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Validate filename and extension
        String filename = file.getOriginalFilename();
        if (filename == null ||
                !(filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg") || filename.toLowerCase().endsWith(".png"))) {
            throw new IllegalArgumentException("Only JPG and PNG images are supported.");
        }

        // Save the file
        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath.toFile());

        // Return public URL
        return urlPrefix + filename;
    }

    /**
     * Deletes a file from the upload directory
     * @param fileUrl the public URL of the uploaded file
     * @throws IOException if deletion fails
     */
    public void deleteFile(String fileUrl) throws IOException {
        // Extract just the file name safely
        String filename = Paths.get(new URL(fileUrl).getPath()).getFileName().toString();

        //Build the full absolute path
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(filename).normalize();

        // Delete the file if it exists
        Files.deleteIfExists(filePath);
    }
}
