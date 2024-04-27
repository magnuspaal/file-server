package com.magnus.fileserver.controllers;

import com.magnus.fileserver.auth.AuthenticationService;
import com.magnus.fileserver.controllers.dto.BaseResponse;
import com.magnus.fileserver.file.FileInfo;
import com.magnus.fileserver.file.FileInfoService;
import com.magnus.fileserver.upload.FileStorageUtils;
import com.magnus.fileserver.user.User;
import com.magnus.fileserver.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatFileController {

  private final AuthenticationService authenticationService;
  private final FileInfoService fileInfoService;
  private final UserService userService;

  @GetMapping("/file")
  public ResponseEntity<byte[]> getChatFile(@RequestParam String chatId, @RequestParam String filename) {
    User user = authenticationService.getAuthenticatedUser();

    FileInfo fileInfo = fileInfoService.getByFilenameAndFilepath(filename, "./private/" + chatId);
    if (!fileInfo.getUsers().contains(user)) {
      return ResponseEntity.status(403).build();
    }
    return ResponseEntity.ok(FileStorageUtils.getFile(Paths.get("./private/" + chatId), filename));
  }

  @PostMapping("/upload")
  public ResponseEntity<BaseResponse> uploadFile(
      @RequestParam(name = "file") MultipartFile file,
      @RequestParam(name = "filename") String filename,
      @RequestParam(name = "imageRatio") String imageRatio,
      @RequestParam(name = "chat") String chat,
      @RequestParam(name = "users") String users
  ) {
    Path path = Paths.get("./private/" + chat);
    FileStorageUtils.uploadFile(file, path, filename);

    List<User> userList = new ArrayList<>();
    for (String userId: users.split(",")) {
      userList.add(userService.getUserById(Long.parseLong(userId)));
    }

    fileInfoService.saveFileInfo(new FileInfo(path.toString(), filename, Integer.parseInt(imageRatio), userList));
    return ResponseEntity.ok().build();
  }
}
