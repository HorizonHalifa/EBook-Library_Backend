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
@SuppressWarnings("unused")
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    // Directory on the server where uploaded files will be saved. (from application.properties)
    @Value("${upload.dir}")
    private String uploadDir;

    // Public URL prefix  used to construct file download links
    @Value("${upload.url-prefix}")
    private String urlPrefix;

    /**
     * Upload a PDF file to the server and return it's public URL.
     * @param file the uploaded file (must be a non-empty PDF)
     * @return a 200 OK response with the file URL, or 400/500 on error
     */
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

            // Define full target patch and write file
            Path filePath = uploadPath.resolve(originalFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Generate public URL
            String fileUrl = urlPrefix + originalFileName;

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File upload failed.");
        }
    }
}
