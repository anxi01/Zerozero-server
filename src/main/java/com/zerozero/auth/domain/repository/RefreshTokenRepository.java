package com.zerozero.auth.domain.repository;

import com.zerozero.auth.domain.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {
}
