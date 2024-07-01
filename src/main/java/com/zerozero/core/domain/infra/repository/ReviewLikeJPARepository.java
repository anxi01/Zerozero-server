package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.ReviewLike;
import com.zerozero.core.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeJPARepository extends JpaRepository<ReviewLike, Long> {

  Optional<ReviewLike> findByUserAndReview(User user, Review review);

  int countByReview(Review review);
}
