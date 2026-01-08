package com.enterprise.automation.util;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import com.enterprise.automation.browser.PlaywrightFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * File upload/download utilities for web automation
 */
public class FileHelper {
    private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);
    private static final String DOWNLOADS_DIR = "target/downloads";
    private static final String UPLOADS_DIR = "src/test/resources/testdata/uploads";

    static {
        createDirectoryIfNotExists(DOWNLOADS_DIR);
        createDirectoryIfNotExists(UPLOADS_DIR);
    }

    private FileHelper() {}

    /**
     * Handle file upload
     */
    public static void uploadFile(String fileInputSelector, String filePath) {
        Page page = PlaywrightFactory.getPage();
        page.locator(fileInputSelector).setInputFiles(Paths.get(filePath));
        logger.info("File uploaded: {}", filePath);
    }

    /**
     * Handle multiple file uploads
     */
    public static void uploadMultipleFiles(String fileInputSelector, String... filePaths) {
        Page page = PlaywrightFactory.getPage();
        Path[] paths = new Path[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            paths[i] = Paths.get(filePaths[i]);
        }
        page.locator(fileInputSelector).setInputFiles(paths);
        logger.info("Multiple files uploaded: {}", filePaths.length);
    }

    /**
     * Download file and return path
     */
    public static String downloadFile(String downloadButtonSelector) throws IOException {
        Page page = PlaywrightFactory.getPage();
        
        Download download = page.waitForDownload(() -> {
            page.locator(downloadButtonSelector).click();
        });
        
        String fileName = download.suggestedFilename();
        String filePath = DOWNLOADS_DIR + "/" + fileName;
        download.saveAs(Paths.get(filePath));
        logger.info("File downloaded: {} to {}", fileName, filePath);
        return filePath;
    }

    /**
     * Get last downloaded file
     */
    public static String getLastDownloadedFile() throws IOException {
        Path downloadPath = Paths.get(DOWNLOADS_DIR);
        
        try (Stream<Path> paths = Files.list(downloadPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .max((p1, p2) -> Long.compare(
                            getFileCreationTime(p1),
                            getFileCreationTime(p2)))
                    .map(Path::toString)
                    .orElseThrow(() -> new IOException("No downloaded files found"));
        }
    }

    /**
     * Get file creation time
     */
    private static long getFileCreationTime(Path path) {
        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * Clear downloads directory
     */
    public static void clearDownloadsDirectory() throws IOException {
        Path downloadPath = Paths.get(DOWNLOADS_DIR);
        if (Files.exists(downloadPath)) {
            Files.list(downloadPath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            logger.warn("Failed to delete file: {}", path);
                        }
                    });
            logger.info("Downloads directory cleared");
        }
    }

    /**
     * Check if file exists
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Get file size in bytes
     */
    public static long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }

    /**
     * Create directory if not exists
     */
    private static void createDirectoryIfNotExists(String dirPath) {
        try {
            Files.createDirectories(Paths.get(dirPath));
        } catch (IOException e) {
            logger.warn("Failed to create directory: {}", dirPath);
        }
    }
}
