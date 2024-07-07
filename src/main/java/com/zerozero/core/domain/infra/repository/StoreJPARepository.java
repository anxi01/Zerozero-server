package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.Store;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreJPARepository extends JpaRepository<Store, UUID> {

  Integer countStoresByUserId(UUID userId);

  @Query("SELECT u.id, COUNT(s) as storeCount FROM User u JOIN Store s ON u.id = s.userId GROUP BY u.id ORDER BY storeCount DESC")
  List<Object[]> findUserIdWithStoreCountOrderByStoreCountDesc();

  Boolean existsByNameAndMapxAndMapyAndStatusIsTrue(String name, int mapx, int mapy);

  Store findByNameAndMapxAndMapyAndStatusIsTrue(String title, int mapx, int mapy);

  List<Store> findAllByUserId(UUID userId);
}