package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * Controller class that:
 * - Accepts multipart/form-data file uploads.
 * - Saves files to the uploads/ folder.
 * - Returns the URL where the file can be accessed.
 */
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    // Inject the value of 'upload.dir' from application.properties
    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @PostMapping("/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        // Validate input
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected.");
        }

        try {
            // Get original file name and clean it
            String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

            // Create the upload path if it doesn't exist
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // Resolve the full file path
            Path filePath = uploadPath.resolve(originalFileName);

            // Copy the file to the destination
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Construct public URL
            String fileUrl = urlPrefix + originalFileName;

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File upload failed.");
        }
    }
}
