package com.zerozero.store.domain.repository;

import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.value.GeoLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    Integer countStoresByUserId(UUID userId);

    @Query(value = """
                SELECT user_rank.r
                FROM (
                    SELECT u.id AS user_id,
                           RANK() OVER (ORDER BY COUNT(s.id) DESC) AS r
                    FROM user u
                    JOIN store s ON u.id = s.user_id
                    GROUP BY u.id
                ) AS user_rank
                WHERE user_rank.user_id = :userId
            """, nativeQuery = true)
    Optional<Integer> findStoreUserRank(@Param("userId") UUID userId);

    Store findByNameAndGeoLocation(String name, GeoLocation geoLocation);

    List<Store> findAllByUserIdAndDeleted(UUID userId, boolean deleted);
}
