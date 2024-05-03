package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.RefreshToken;
import com.zerozero.core.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByUser(User user);
}
