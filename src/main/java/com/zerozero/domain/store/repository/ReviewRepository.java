package com.zerozero.domain.store.repository;

import com.zerozero.domain.store.domain.Review;
import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  boolean existsByUserAndStore(User user, Store store);
}
