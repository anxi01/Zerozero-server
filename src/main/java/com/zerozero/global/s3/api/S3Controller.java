package com.zerozero.global.s3.api;

import com.zerozero.global.common.dto.response.ApiResponse;
import com.zerozero.global.s3.application.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image Controller", description = "이미지 관련 API입니다.")
@RestController
@RequiredArgsConstructor
public class S3Controller {

  private final S3Service s3Service;

  @Operation(summary = "이미지 업로드", description = "판매점 등록시 이미지를 같이 업로드합니다.")
  @PostMapping("/upload-image")
  public ApiResponse<List<String>> uploadImage(List<MultipartFile> multipartFiles) {
    return ApiResponse.ok(s3Service.uploadImage(multipartFiles));
  }
}