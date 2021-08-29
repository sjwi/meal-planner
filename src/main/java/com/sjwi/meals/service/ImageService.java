package com.sjwi.meals.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageService {
  
  @Value("${meals.files.imagePath}")
  String imagePath;

  @Value("${meals.files.urlBase}")
  String urlBase;

  private static final String IMAGE_FILE_PREFIX = "recipe_";

  public List<String> storeFiles(MultipartFile[] imageFiles, Integer mealId) throws IOException {
    final Path root = Paths.get(imagePath);
    List<String> fileNames = new ArrayList<>();
    Integer counter = 0;
    for(MultipartFile file : imageFiles) {
      String extension = FilenameUtils.getExtension(file.getOriginalFilename());
      String fileName = IMAGE_FILE_PREFIX + mealId + "_" + counter++ + "." + extension;
      Files.copy(file.getInputStream(), root.resolve(fileName));
      fileNames.add(urlBase + "/" + fileName);
    }
    return fileNames;
  }

}
