package com.zerozero.review.domain.service;

import com.zerozero.review.domain.model.Review;
import com.zerozero.review.domain.repository.ReviewRepository;
import com.zerozero.review.exception.ReviewErrorType;
import com.zerozero.review.exception.ReviewException;
import com.zerozero.review.presentation.request.ReviewRequest;
import com.zerozero.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class UpdateStoreReviewUseCase {

    private final ReviewRepository reviewRepository;

    public void execute(UUID reviewId, ReviewRequest reviewRequest, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ReviewErrorType.NOT_EXIST_REVIEW));
        if (!review.isWrittenBy(user.getId())) {
            log.error("[UpdateStoreReviewUseCase] User does not have review with id {}", reviewId);
            throw new ReviewException(ReviewErrorType.USER_VALIDATION_FAILED);
        }
        review.updateContent(reviewRequest.content());
        review.updateZeroDrinks(reviewRequest.zeroDrinks());
    }

}
