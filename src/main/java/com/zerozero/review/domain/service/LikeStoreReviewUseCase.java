package com.zerozero.review.domain.service;

import com.zerozero.review.domain.model.Review;
import com.zerozero.review.domain.model.ReviewLike;
import com.zerozero.review.domain.repository.ReviewLikeRepository;
import com.zerozero.review.domain.repository.ReviewRepository;
import com.zerozero.review.exception.ReviewErrorType;
import com.zerozero.review.exception.ReviewException;
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
public class LikeStoreReviewUseCase {

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    public void execute(UUID reviewId, User user) {
        Review review = reviewRepository.findByIdAndDeleted(reviewId, false)
                .orElseThrow(() -> new ReviewException(ReviewErrorType.NOT_EXIST_DELETABLE_REVIEW));

        reviewLikeRepository.findByReviewIdAndUserIdAndDeleted(review.getId(), user.getId(), false)
                .ifPresentOrElse(
                        existingLike -> {
                            log.info("[Unlike] User {} unliked Review {}", user.getId(), review.getId());
                            reviewLikeRepository.delete(existingLike);
                        },
                        () -> {
                            log.info("[Like] User {} liked Review {}", user.getId(), review.getId());
                            ReviewLike newLike = ReviewLike.create(review.getId(), user.getId());
                            reviewLikeRepository.save(newLike);
                        }
                );
    }

}
