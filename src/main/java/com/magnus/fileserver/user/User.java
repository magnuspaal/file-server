package com.magnus.fileserver.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String username;
}
