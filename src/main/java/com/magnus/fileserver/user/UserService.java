package com.magnus.fileserver.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User getUserById(Long id) {
    return userRepository.findById(id).orElse(userRepository.save(new User(id)));
  }

  public User updateOrCreateUser(User newUser) {
    User user = userRepository.findById(newUser.getId()).orElse(null);
    if (user == null) {
      return userRepository.save(newUser);
    } else {
      user.setEmail(newUser.getEmail());
      user.setFirstName(newUser.getFirstName());
      user.setLastName(newUser.getLastName());
      user.setUsername(newUser.getUsername());
      return userRepository.save(user);
    }
  }
}
