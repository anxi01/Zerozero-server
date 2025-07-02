package com.zerozero.store.domain.repository

import com.zerozero.store.domain.model.Store
import com.zerozero.store.domain.response.StoreUserRankProjection
import com.zerozero.store.domain.value.GeoLocation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface StoreRepository : JpaRepository<Store, UUID> {

    @Query(
        """
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
            """, nativeQuery = true
    )
    fun findStoreUserRank(@Param("userId") userId: UUID): Optional<StoreUserRankProjection>

    @Query(
        """
                SELECT s FROM Store s
                LEFT JOIN FETCH s.images
                WHERE s.id = :storeId
            
            """
    )
    fun findByIdWithImages(@Param("storeId") storeId: UUID): Optional<Store>

    fun findByNameAndGeoLocation(name: String, geoLocation: GeoLocation): Store?

    @Query(
        """
                SELECT s FROM Store s
                LEFT JOIN FETCH s.images
                WHERE s.userId = :userId
            
            """
    )
    fun findAllByUserIdWithImages(@Param("userId") userId: UUID): List<Store>
}
