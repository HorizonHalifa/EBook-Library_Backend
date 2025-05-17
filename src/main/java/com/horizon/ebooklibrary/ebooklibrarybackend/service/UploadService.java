package com.horizon.ebooklibrary.ebooklibrarybackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

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


    /*
     * The following savePdf and saveImage methods, generate a unique name for uploaded files with timestamps.
     * The reason this is necessary is that saving into the system different files with the same name might result in overrides.
     * Therefore, a unique naming convention is necessary and saving according to timestamp is one possible way to integrate this that seemed most reasonable at the time.
     */

    /**
     * Saves a PDF file to the upload directory with a unique timestamped filename.
     * @param file The uploaded PDF file
     * @return public URL to the stored file
     * @throws IOException if saving fails
     */
    public String savePdf(MultipartFile file) throws IOException {
        ensureUploadDirExists();

        // Validate filename and extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are supported.");
        }

        // Get a unique time stamp file name
        String uniqueFilename = generateTimeStampedFilename(originalFilename);

        // Save the file to disk
        Path filePath = getUploadPath().resolve(uniqueFilename);
        file.transferTo(filePath.toFile());

        // Return public URL to access the file
        return urlPrefix + uniqueFilename;
    }

    /**
     * Saves an image file (JPG, PNG) with a unique timestamped filename.
     * @param file The uploaded image file
     * @return public URL to the stored file
     * @throws IOException if saving fails
     */
    public String saveImage(MultipartFile file) throws IOException {
        ensureUploadDirExists();

        // Validate filename and extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null ||
                !(originalFilename.toLowerCase().endsWith(".jpg")
                        || originalFilename.toLowerCase().endsWith(".jpeg")
                        || originalFilename.toLowerCase().endsWith(".png"))) {
            throw new IllegalArgumentException("Only JPG and PNG images are supported.");
        }

        // Get a unique time stamp file name
        String uniqueFilename = generateTimeStampedFilename(originalFilename);

        // Save the file
        Path filePath = getUploadPath().resolve(uniqueFilename);
        file.transferTo(filePath.toFile());

        // Return public URL
        return urlPrefix + uniqueFilename;
    }

    /**
     * Deletes a file from the upload directory
     * @param fileUrl the public URL of the uploaded file
     * @throws IOException if deletion fails
     */
    public void deleteFile(String fileUrl) throws IOException {

        try {
            // Parse the file URL to extract the filename
            URI uri = new URI(fileUrl);
            String filename = Paths.get(uri.getPath()).getFileName().toString();

            // Resolve the full path to get the file
            Path filePath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(filename).normalize();

            // Delete the file if it exists
            Files.deleteIfExists(filePath);
        } catch(URISyntaxException e) {
            throw new IOException("Invalid file URL: " + fileUrl, e);
        }
    }

    /**
     * Ensures that the upload directory exists.
     * Creates it if necessary
     */
    private void ensureUploadDirExists() throws IOException {
        Path uploadPath = getUploadPath();
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    /**
     * @return the full upload path
     */
    private Path getUploadPath() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Appends a timestamp to the filename to ensure uniqueness.
     * @param originalFilename original file name from client
     * @return modified filename with timestamp
     */
    private String generateTimeStampedFilename(String originalFilename) {
        String baseName = originalFilename.substring(0, originalFilename.lastIndexOf(".")); // Saves the name of the file without the type after the '.' .
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // Saves the type of the file that is after the '.' .
        long timeStamp = Instant.now().toEpochMilli(); // Get some time reference long number for the java time framework

        return baseName + "_" + timeStamp + extension; // Builds the file so it returns "originalFileName_generatedTimeStamp.fileType"
    }
}
