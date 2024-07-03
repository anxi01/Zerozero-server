package com.zerozero.core.domain.infra.repository;

import com.zerozero.core.domain.entity.Store;
import com.zerozero.core.domain.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreJPARepository extends JpaRepository<Store, UUID> {

  Long countStoresByUser(User user);

  @Query("SELECT s.user.id, COUNT(s) as storeCount FROM Store s GROUP BY s.user.id ORDER BY storeCount DESC")
  List<Object[]> countStoresByUserId();

  Boolean existsByNameAndMapxAndMapyAndSellingIsTrue(String name, int mapx, int mapy);

  List<Store> findAllByUser(User user);

  Store findByNameAndMapxAndMapyAndSellingIsTrue(String title, int mapx, int mapy);
}