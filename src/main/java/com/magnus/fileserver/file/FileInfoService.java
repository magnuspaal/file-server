package com.magnus.fileserver.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FileInfoService {
  private final FileInfoRepository fileInfoRepository;

  public void saveFileInfo(FileInfo fileInfo) {
    fileInfoRepository.save(fileInfo);
  }

  public FileInfo getByFilenameAndFilepath(String filename, String filepath) {
    return fileInfoRepository.findByFilenameAndFilepath(filename, filepath).orElseThrow(() -> new NoSuchElementException("FileInfo not found"));
  }
}
