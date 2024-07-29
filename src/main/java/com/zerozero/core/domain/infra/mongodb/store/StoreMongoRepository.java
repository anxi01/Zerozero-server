package com.zerozero.core.domain.infra.mongodb.store;

import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreMongoRepository extends MongoRepository<Store, UUID> {

}
