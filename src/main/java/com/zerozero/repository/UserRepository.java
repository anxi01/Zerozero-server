package com.zerozero.repository;

import com.zerozero.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  boolean existsByNickname(String nickname);
  boolean existsByEmail(String email);
}
