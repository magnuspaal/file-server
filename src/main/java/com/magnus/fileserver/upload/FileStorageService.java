package com.magnus.fileserver.upload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.magnus.fileserver.utils.FileUtils;
import com.magnus.fileserver.utils.RandomStringGenerator;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

@Service
public class FileStorageService {

  private final Path fileStorageLocation;

  @Autowired
  public FileStorageService(Environment env) {
    this.fileStorageLocation = Paths.get(Objects.requireNonNull(env.getProperty("app.upload-dir")))
        .toAbsolutePath().normalize();

    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new RuntimeException(
          "Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  public String storeFile(MultipartFile file) {
    try {

      Scalr.Rotation rotation = this.getImageRotation(file);

      BufferedImage inputImage = ImageIO.read(file.getInputStream());

      if (rotation != null) {
        inputImage = Scalr.rotate(inputImage, rotation, Scalr.OP_ANTIALIAS);
      }

      inputImage = Scalr.resize(inputImage, Scalr.Mode.FIT_TO_WIDTH, 558);

      // Normalize file name
      String namePrefix = RandomStringGenerator.getRandomString() + "-" + new Date().getTime() + "-" + inputImage.getWidth() + "_" + inputImage.getHeight();

      String fileName = namePrefix + "." + FileUtils.getFileExtension(file.getOriginalFilename());

      // Check if the filename contains invalid characters
      if (fileName.contains("..")) {
        throw new RuntimeException(
            "Sorry! Filename contains invalid path sequence " + fileName);
      }

      this.uploadImage(inputImage, fileName);

      return fileName;
    } catch (IOException ex) {
      throw new RuntimeException("Could not store file", ex);
    } catch (ImageProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private void uploadImage(BufferedImage inputImage, String fileName) throws IOException {

    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(FileUtils.getFileExtension(fileName));
    ImageWriter writer = writers.next();

    Path targetLocation = this.fileStorageLocation.resolve(fileName);
    File outputFile = new File(targetLocation.toUri());

    ImageOutputStream outputStream = ImageIO.createImageOutputStream(outputFile);
    writer.setOutput(outputStream);

    ImageWriteParam params = writer.getDefaultWriteParam();
    params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    params.setCompressionQuality(1);

    writer.write(null, new IIOImage(inputImage, null, null), params);

    outputStream.close();
    writer.dispose();
  }

  private Scalr.Rotation getImageRotation(MultipartFile file) throws IOException, ImageProcessingException {
    Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
    ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);

    int orientation;

    try {
      orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
    } catch (MetadataException exception) {
      return null;
    }

    switch (orientation) {
      // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
      case 6 -> { // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
        return Scalr.Rotation.CW_90;
      }
      case 3 -> { // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
        return Scalr.Rotation.CW_180;
      }
      case 8 -> { // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
        return Scalr.Rotation.CW_270;
      }
      default -> {
        return null;
      }
    }
  }

  public void deleteFile(String fileName) throws IOException {
    Path targetLocation = this.fileStorageLocation.resolve(fileName);
    Files.delete(targetLocation);
  }
}