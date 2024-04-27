package com.magnus.fileserver.upload;

import com.magnus.fileserver.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;

public class FileStorageUtils {
  public static void uploadFile(MultipartFile file, Path path, String filename) {
    try {
      Files.createDirectories(path);
      byte[] bytes = file.getInputStream().readAllBytes();

      String string = Base64.getEncoder().encodeToString(bytes);

      Path targetLocation = path.resolve(filename);
      File outputFile = new File(targetLocation.toUri());
      try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8)) {
        writer.write(string);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean fileBelongsToUser(User user, String filename) {
    String filenameWithoutFormat = filename.split("\\.")[0];
    String users = filenameWithoutFormat.split("_")[1];
    String[] userList = users.split("-");
    return Arrays.asList(userList).contains(user.getId().toString());
  }

  public static byte[] getFile(Path path, String filename) {
    Path targetLocation = path.resolve(filename);
    try {
      return Files.readAllBytes(targetLocation);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
