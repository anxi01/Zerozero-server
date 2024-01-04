package com.zerozero.domain.store.repository;

import com.zerozero.domain.store.domain.Store;
import com.zerozero.domain.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

  List<Store> findStoresByUser(User user);
}
