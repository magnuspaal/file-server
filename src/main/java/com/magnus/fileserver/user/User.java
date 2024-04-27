package com.magnus.fileserver.user;

import com.magnus.fileserver.common.BaseEntity;
import com.magnus.fileserver.file.FileInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_data")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {
  @Id
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String username;

  @ManyToMany
  private List<FileInfo> fileInfoList;

  public User(Long id) {
    this.id = id;
  }

  public User(Long id, String firstName, String lastName, String email, String username) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
  }

  public boolean equals(Object o) {
    if (!(o instanceof User user)) {
      return false;
    }
    return this.id.equals(user.getId());
  }

  @Override
  public int hashCode() {
    return this.id.hashCode();
  }
}
