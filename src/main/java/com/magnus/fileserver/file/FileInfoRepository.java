package com.magnus.fileserver.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
  Optional<FileInfo> findByFilenameAndFilepath(@Param("filename") String filename, @Param("filepath") String filepath);
}