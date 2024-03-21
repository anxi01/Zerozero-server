package com.zerozero.domain.store.repository;

import com.zerozero.domain.store.domain.Review;
import com.zerozero.domain.store.domain.ReviewLike;
import com.zerozero.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

  Optional<ReviewLike> findByUserAndReview(User user, Review review);

  int countByReview(Review review);
}
