package com.horizon.ebooklibrary.ebooklibrarybackend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Controller class to serve uploaded PDF files from the filesystem
 * Allows the android app to load images and pdfs from URLs
 * This controller is what allows those URLs to work by mapping: GET /files/{filename}
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/files")
public class FileServingController {

    @Value("${upload.dir}")
    private String uploadDir;

    /**
     * Serves a PDF file from the local upload directory
     * <p>
     * URL: GET /files/{filename}
     * Example: /files/cat_wizard.pdf
     * <p>
     * This allows  public access to previously uploaded files,
     * using the URL stored in the Book's 'pdfUrl' field.
     *
     * @param filename The name of the file to retrieve
     * @return A ResponseEntity containing the PFG as a streamed resource, or an error response
     */
    @GetMapping("/{filename:.+}") // Match the filename with a dot in it (like .pdf) and let it include extensions
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            /*
             * Resolve the absolute file path
             * Combines the upload directory path with the requested filename
             */
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();

            /*
             * Load the file as a Spring Resource from the file system
             * UrlResource can represent a local file as a downloadable HTTP resource
             */
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the file exists. If it doesn't return '404 Not Found'
            if(!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            /*
             * Attempt to detect the MIME type (e.g. application/pdf)
             * It's useful so that browsers know how to handle the file (open it or download)
             */
            String contentType = Files.probeContentType(filePath);
            if(contentType == null) {
                contentType = "application/pdf"; // fallback: Default to PDF if undetectable
            }

            // Return the file with HTTP 200 OK and appropriate headers
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType)) // Set Content-Type header
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"") // Display in-browser
                    .body(resource); // Body contains the file itself

        } catch(MalformedURLException e) {
            // If the file path was invalid, return '400 Bad Request'
            return ResponseEntity.badRequest().build();

        } catch(Exception e) {
            // A catch-all for any IO or unexpected issues. Returns '500 Internal Server Error'
            return ResponseEntity.internalServerError().build();
        }
    }
}
