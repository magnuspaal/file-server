package com.magnus.fileserver.utils;

public class FileUtils {

  public static String getFileExtension(String fileName) {
    if (fileName == null) {
      return "";
    }
    String[] fileNameParts = fileName.split("\\.");

    return fileNameParts[fileNameParts.length - 1];
  }

}
