package com.magnus.fileserver.controllers;

import com.magnus.fileserver.upload.FileStorageService;
import com.magnus.fileserver.upload.UploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class FileUploadController {
  private final FileStorageService fileStorageService;

  public FileUploadController(FileStorageService fileStorageService) {
    this.fileStorageService = fileStorageService;
  }

  @PostMapping("/upload")
  public ResponseEntity<UploadResponse> uploadFile(
      @RequestParam(name = "file", required = true) MultipartFile file
  ) {
    String fileName = fileStorageService.storeFile(file);

    UploadResponse uploadResponse = new UploadResponse(fileName);

    return ResponseEntity.ok().body(uploadResponse);
  }

  @DeleteMapping("/delete/{fileName}")
  public ResponseEntity<Void> deleteFile(
      @PathVariable String fileName
  ) {
    try {
      fileStorageService.deleteFile(fileName);
      return ResponseEntity.ok().build();
    } catch (IOException e) {
      return ResponseEntity.status(410).build();
    }
  }
}
