package com.chromeExtensions.controllers;

import com.chromeExtensions.services.Interfaces.IFileMonitorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file-monitor")
@CrossOrigin(origins = "*")
public class FileMonitorController {

    private final IFileMonitorService fileMonitorService;

    public FileMonitorController(IFileMonitorService fileMonitorService) {
        this.fileMonitorService = fileMonitorService;
    }

    /**
     * Endpoint called by the Chrome extension to determine if a file should be blocked.
     * @param file MultipartFile (expected to be first 16 bytes of the download)
     * @return true to block, false to allow
     */
    @PostMapping("/should-block")
    public ResponseEntity<Boolean> shouldBlockFileDownload(@RequestParam("file") MultipartFile file) {
        try {
            boolean shouldBlock = fileMonitorService.shouldBlockFileDownload(file);
            return ResponseEntity.ok(shouldBlock);
        } catch (Exception e) {
            // if an error occurs, block the file
            return ResponseEntity.ok(true);
        }
    }
}
