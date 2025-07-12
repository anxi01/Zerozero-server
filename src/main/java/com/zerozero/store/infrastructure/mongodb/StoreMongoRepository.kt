package com.zerozero.store.infrastructure.mongodb

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface StoreMongoRepository : MongoRepository<Store, UUID> {

    @Query("{ 'location': { \$near: { \$geometry: { type: 'Point', coordinates: [?0, ?1] }, \$maxDistance: ?2 } } }")
    fun findStoresWithinCoordinatesRadius(longitude: Double, latitude: Double, maxDistance: Double): List<Store>
}
