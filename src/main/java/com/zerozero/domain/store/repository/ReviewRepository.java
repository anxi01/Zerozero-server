package com.zerozero.domain.store.repository;

import com.zerozero.domain.store.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
