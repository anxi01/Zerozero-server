package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.ReviewLike;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeJPARepository extends JpaRepository<ReviewLike, Long> {

  ReviewLike findByReviewIdAndUserIdAndDeleted(UUID reviewId, UUID userId, Boolean deleted);

  Integer countByReviewIdAndDeleted(UUID reviewId, Boolean deleted);

  Boolean existsByReviewIdAndUserIdAndDeleted(UUID reviewId, UUID userId, Boolean deleted);
}
