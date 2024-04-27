package com.magnus.fileserver.file;

import com.magnus.fileserver.common.BaseEntity;
import com.magnus.fileserver.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "file_info")
@SQLRestriction("deleted_at IS NULL")
public class FileInfo extends BaseEntity {
  @Id
  @SequenceGenerator(name = "file_info_sequence", sequenceName = "file_info_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_info_sequence")
  private Long id;

  private String filepath;

  private String filename;

  private Integer imageRatio;

  @ManyToMany
  @JoinTable(
      name = "users_file_info",
      joinColumns = @JoinColumn(name = "file_info_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> users;

  public FileInfo(String filepath, String filename, Integer imageRatio, List<User> users) {
    this.filepath = filepath;
    this.filename = filename;
    this.imageRatio = imageRatio;
    this.users = users;
  }
}
