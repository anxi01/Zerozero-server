package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJPARepository extends JpaRepository<User, UUID> {

  User findByEmail(String email);

  Boolean existsByNickname(String nickname);

  Boolean existsByEmail(String email);
}
