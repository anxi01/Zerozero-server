package com.zerozero.domain.user.repository;

import com.zerozero.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByNickname(String nickname);

  boolean existsByNickname(String nickname);

  boolean existsByEmail(String email);
}
