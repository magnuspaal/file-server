package com.magnus.fileserver.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UploadResponse {
  private String fileName;
}