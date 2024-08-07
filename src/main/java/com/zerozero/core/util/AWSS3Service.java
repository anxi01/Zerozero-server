package com.zerozero.core.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AWSS3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  public String uploadImage(MultipartFile multipartFile) throws IOException {
    String fileName = createFileName(multipartFile.getOriginalFilename());

    String fileExtension = getFileExtension(fileName);

    if (isValidImageFileExtension(fileExtension)) {

      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(multipartFile.getSize());
      objectMetadata.setContentType(multipartFile.getContentType());

      amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);
      return getUrl(bucket, fileName);
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원되지 않는 이미지 형식입니다.");
    }
  }

  public List<String> uploadImages(List<MultipartFile> multipartFiles) {
    List<String> imageUrls = new ArrayList<>();

    multipartFiles.forEach(file -> {
          String fileName = createFileName(file.getOriginalFilename());
          String fileExtension = getFileExtension(fileName);

      if (isValidImageFileExtension(fileExtension)) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
          amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }
        imageUrls.add(getUrl(bucket, fileName));
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원되지 않는 이미지 형식입니다.");
      }
    });
    return imageUrls;
  }

  private String createFileName(String fileName){
    return UUID.randomUUID().toString().concat(getFileExtension(fileName));
  }

  private String getFileExtension(String fileName){
    try{
      return fileName.substring(fileName.lastIndexOf("."));
    } catch (StringIndexOutOfBoundsException e){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
    }
  }

  private String getUrl(String bucket, String fileName) {
    return amazonS3.getUrl(bucket, fileName).toString();
  }

  private boolean isValidImageFileExtension(String fileExtension) {
    return fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpeg")
        || fileExtension.equalsIgnoreCase(".jpg");
  }
}
