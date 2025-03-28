package com.zerozero.review.domain.service;

import com.zerozero.review.domain.model.Review;
import com.zerozero.review.domain.repository.ReviewRepository;
import com.zerozero.review.exception.ReviewErrorType;
import com.zerozero.review.exception.ReviewException;
import com.zerozero.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class DeleteStoreReviewUseCase {

    private final ReviewRepository reviewRepository;

    public void execute(UUID reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorType.NOT_EXIST_DELETABLE_REVIEW));
        if (!review.isWrittenBy(user.getId())) {
            log.error("[DeleteStoreReviewUseCase] User does not have review with id {}", reviewId);
            throw new ReviewException(ReviewErrorType.USER_VALIDATION_FAILED);
        }
        reviewRepository.delete(review);
    }

}
