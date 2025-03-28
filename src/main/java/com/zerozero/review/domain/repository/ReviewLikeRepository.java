package com.zerozero.review.domain.repository;

import com.zerozero.review.domain.model.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewIdAndUserId(UUID reviewId, UUID userId);

}
