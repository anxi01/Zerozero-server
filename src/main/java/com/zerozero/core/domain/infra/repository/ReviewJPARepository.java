package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.Review;
import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJPARepository extends JpaRepository<Review, UUID> {

  boolean existsByUserAndStore(User user, Store store);

  List<Review> findAllByStore(Store store);
}
