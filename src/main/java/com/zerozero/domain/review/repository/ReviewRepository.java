package com.zerozero.domain.review.repository;

import com.zerozero.domain.review.domain.Review;
import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  boolean existsByUserAndStore(User user, Store store);

  List<Review> findAllByStore(Store store);
}
