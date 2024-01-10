package com.zerozero.global.s3.api;

import com.zerozero.global.common.dto.response.ApiResponse;
import com.zerozero.global.s3.application.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

  private final S3Service s3Service;

  @PostMapping("/upload-image")
  public ApiResponse<List<String>> uploadImage(List<MultipartFile> multipartFiles) {
    return ApiResponse.ok(s3Service.uploadImage(multipartFiles));
  }
}