package com.magnus.fileserver.controllers;

import com.magnus.fileserver.auth.AuthenticationService;
import com.magnus.fileserver.controllers.dto.BaseResponse;
import com.magnus.fileserver.upload.FileStorageUtils;
import com.magnus.fileserver.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatFileController {

  private final AuthenticationService authenticationService;

  @GetMapping("/chat/file")
  public ResponseEntity<byte[]> getChatFile(@RequestParam String chatId, @RequestParam String filename) {
    User user = authenticationService.getAuthenticatedUser();
    if (!FileStorageUtils.fileBelongsToUser(user, filename)) {
      return ResponseEntity.status(403).build();
    }
    return ResponseEntity.ok(FileStorageUtils.getFile(Paths.get("./private/" + chatId), filename));
  }

  @PostMapping("/chat/upload")
  public ResponseEntity<BaseResponse> uploadFile(
      @RequestParam(name = "file") MultipartFile file,
      @RequestParam(name = "filename") String filename,
      @RequestParam(name = "chat") String chat
  ) {
    FileStorageUtils.uploadFile(file, Paths.get("./private/" + chat), filename);
    return ResponseEntity.ok().build();
  }
}
