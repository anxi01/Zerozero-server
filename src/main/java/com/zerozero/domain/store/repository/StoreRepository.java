package com.zerozero.domain.store.repository;

import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long> {

  List<Store> findStoresByUser(User user);

  @Query("SELECT s.user.id, COUNT(s) as storeCount FROM Store s GROUP BY s.user.id ORDER BY storeCount DESC")
  List<Object[]> countStoresByUserId();
}
