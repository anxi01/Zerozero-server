package com.zerozero.domain.store.api;

import com.zerozero.domain.store.application.ReviewService;
import com.zerozero.domain.store.dto.request.ReviewRequest;
import com.zerozero.global.common.dto.response.ApiResponse;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/{storeId}")
  private ApiResponse<String> uploadReview(Principal connectedUser, @PathVariable Long storeId,
      @RequestBody ReviewRequest request) {

    reviewService.uploadReview(connectedUser, storeId, request);
    return ApiResponse.ok();
  }

  @PatchMapping("/edit/{reviewId}")
  private ApiResponse<String> editReview(Principal connectedUser, @PathVariable Long reviewId,
      @RequestBody ReviewRequest request) {

    reviewService.editReview(connectedUser, reviewId, request);
    return ApiResponse.ok();
  }
}
