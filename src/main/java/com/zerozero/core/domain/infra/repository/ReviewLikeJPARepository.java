package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.ReviewLike;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeJPARepository extends JpaRepository<ReviewLike, Long> {

  ReviewLike findByReviewIdAndUserId(UUID reviewId, UUID userId);

  Integer countByReviewId(UUID reviewId);
}
