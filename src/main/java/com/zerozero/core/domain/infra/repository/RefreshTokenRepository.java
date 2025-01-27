package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.RefreshToken;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {
}
