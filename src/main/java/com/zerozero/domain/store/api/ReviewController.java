package com.zerozero.domain.store.api;

import com.zerozero.domain.store.application.ReviewService;
import com.zerozero.domain.store.dto.request.ReviewRequest;
import com.zerozero.global.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review Controller", description = "리뷰 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @Operation(summary = "리뷰 등록", description = "사용자가 등록된 판매점에 리뷰를 등록합니다.")
  @PostMapping("/{storeId}")
  private ApiResponse<String> uploadReview(Principal connectedUser, @PathVariable Long storeId,
      @RequestBody ReviewRequest request) {

    reviewService.uploadReview(connectedUser, storeId, request);
    return ApiResponse.ok();
  }

  @Operation(summary = "리뷰 수정", description = "사용자가 작성한 리뷰를 수정할 수 있습니다.")
  @PatchMapping("/edit/{reviewId}")
  private ApiResponse<String> editReview(Principal connectedUser, @PathVariable Long reviewId,
      @RequestBody ReviewRequest request) {

    reviewService.editReview(connectedUser, reviewId, request);
    return ApiResponse.ok();
  }

  @Operation(summary = "리뷰 삭제", description = "사용자가 작성한 리뷰를 삭제할 수 있습니다.")
  @DeleteMapping("/{reviewId}")
  private ApiResponse<String> deleteReview(Principal connectedUser, @PathVariable Long reviewId) {

    reviewService.deleteReview(connectedUser, reviewId);
    return ApiResponse.ok();
  }

  @Operation(summary = "리뷰 좋아요", description = "사용자가 리뷰에 좋아요를 등록, 해제할 수 있습니다.")
  @PatchMapping("/likes/{reviewId}")
  private ApiResponse<String> likeReview(Principal connectedUser, @PathVariable Long reviewId) {

    reviewService.likeReview(connectedUser, reviewId);
    return ApiResponse.ok();
  }
}
