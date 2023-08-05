package com.magnus.fileserver.controllers;

import com.magnus.fileserver.upload.FileStorageService;
import com.magnus.fileserver.upload.UploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
