package com.zerozero.core.domain.infra.mongodb.region;

import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegionMongoRepository extends MongoRepository<Region, UUID> {

}
