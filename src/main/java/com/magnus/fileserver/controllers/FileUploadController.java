package com.magnus.fileserver.controllers;

import com.magnus.fileserver.config.AppProperties;
import com.magnus.fileserver.upload.ImageStorageUtils;
import com.magnus.fileserver.upload.UploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileUploadController {

  private final AppProperties appProperties;

  @PostMapping("/upload")
  public ResponseEntity<UploadResponse> uploadImage(@RequestParam(name = "file") MultipartFile file) {
    String fileName = ImageStorageUtils.storeImage(file, Paths.get(appProperties.getUploadDir()));
    UploadResponse uploadResponse = new UploadResponse(fileName);
    return ResponseEntity.ok().body(uploadResponse);
  }

  @DeleteMapping("/delete/{fileName}")
  public ResponseEntity<Void> deleteFile(@PathVariable String fileName) {
    try {
      ImageStorageUtils.deleteImage(fileName, Paths.get(appProperties.getUploadDir()));
      return ResponseEntity.ok().build();
    } catch (IOException e) {
      return ResponseEntity.status(410).build();
    }
  }
}
