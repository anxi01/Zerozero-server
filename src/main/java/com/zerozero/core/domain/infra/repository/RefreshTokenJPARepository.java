package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.RefreshToken;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJPARepository extends JpaRepository<RefreshToken, Long> {
  RefreshToken findByUserId(UUID userId);
}
