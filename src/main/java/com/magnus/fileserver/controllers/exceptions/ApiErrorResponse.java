package com.magnus.fileserver.controllers.exceptions;

import lombok.*;

import java.util.ArrayList;

@EqualsAndHashCode()
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
  private String message;
  private ArrayList<String> codes;

  public ApiErrorResponse(String message) {
    this.message = message;
  }
}
