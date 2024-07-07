package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJPARepository extends JpaRepository<Review, UUID> {

  Boolean existsByUserIdAndStoreId(UUID userId, UUID storeId);

  Review findByIdAndDeleted(UUID id, Boolean deleted);

  List<Review> findAllByStoreIdAndDeleted(UUID storeId, Boolean deleted);
}
