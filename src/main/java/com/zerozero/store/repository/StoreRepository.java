package com.zerozero.store.repository;

import com.zerozero.store.entity.Store;
import com.zerozero.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

  List<Store> findStoresByUser(User user);
}
