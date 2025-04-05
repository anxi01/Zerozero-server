package com.zerozero.store.domain.repository;

import com.zerozero.store.domain.model.Store;
import com.zerozero.store.domain.response.StoreUserRankProjection;
import com.zerozero.store.domain.value.GeoLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {

    @Query(value = """
                SELECT ranked_user.rank, ranked_user.storeReportCount
                FROM (
                         SELECT
                             s.user_id AS user_id,
                             COUNT(s.id) AS storeReportCount,
                             RANK() OVER (ORDER BY COUNT(s.id) DESC) AS `rank`
                         FROM store s
                         WHERE s.deleted = false
                         GROUP BY user_id
                     ) AS ranked_user
                WHERE ranked_user.user_id = :userId
            """, nativeQuery = true)
    Optional<StoreUserRankProjection> findStoreUserRank(@Param("userId") UUID userId);

    @Query("""
                SELECT s FROM Store s
                LEFT JOIN FETCH s.images
                WHERE s.id = :storeId
            """)
    Optional<Store> findByIdWithImages(@Param("storeId") UUID storeId);

    Store findByNameAndGeoLocation(String name, GeoLocation geoLocation);

    @Query("""
                SELECT s FROM Store s
                LEFT JOIN FETCH s.images
                WHERE s.userId = :userId
            """)
    List<Store> findAllByUserIdWithImages(@Param("userId") UUID userId);
}
