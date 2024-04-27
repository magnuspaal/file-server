package com.magnus.fileserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
  private String uploadDir;
  private String mediaPath;
  private String mediaMapping;
  private String jwtSecret;
  private String allowedOrigins;

  public List<String> getAllowedOrigins() {
    return List.of(allowedOrigins.split(","));
  }
}