package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPARepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  boolean existsByNickname(String nickname);
  boolean existsByEmail(String email);
}
