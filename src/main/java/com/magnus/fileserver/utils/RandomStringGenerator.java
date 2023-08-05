package com.magnus.fileserver.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomStringGenerator {
  static String alphabet = "abcdefghijklmnopqrstubwxyz";
  static StringBuilder sb = new StringBuilder();
  static Random random = new Random();
  static int length = 16;

  public static String getRandomString() {
    for (int i = 0; i < length; i++) {
      int index = random.nextInt(alphabet.length());
      char randomChar = alphabet.charAt(index);
      sb.append(randomChar);
    }
    String string = sb.toString();
    sb.setLength(0);
    return string;
  }
}