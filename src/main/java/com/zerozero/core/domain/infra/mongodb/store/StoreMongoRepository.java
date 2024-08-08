package com.zerozero.core.domain.infra.mongodb.store;

import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface StoreMongoRepository extends MongoRepository<Store, UUID> {

  @Query("{ 'location': { $near: { $geometry: { type: 'Point', coordinates: [?0, ?1] }, $maxDistance: ?2 } } }")
  List<Store> findStoresWithinCoordinatesRadius(double longitude, double latitude, double maxDistance);
}
