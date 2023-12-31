package com.zerozero.store.controller;

import com.zerozero.common.response.ApiResponse;
import com.zerozero.store.dto.request.RegisterStoreRequest;
import com.zerozero.store.dto.response.StoreInfoResponse;
import com.zerozero.store.dto.response.StoreListResponse;
import com.zerozero.store.service.StoreService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  @GetMapping("/search")
  public ApiResponse<StoreListResponse> getStores(@RequestParam String query) {
    return ApiResponse.ok(storeService.search(query));
  }

  @PostMapping
  public ApiResponse<StoreInfoResponse> addStore(Principal connectedUser,
      @RequestParam String query,
      @RequestBody RegisterStoreRequest request) {
    return ApiResponse.ok(storeService.add(connectedUser, query, request));
  }

  @GetMapping("/{storeId}")
  public ApiResponse<StoreInfoResponse> getStoreInfo(@PathVariable Long storeId) {
    return ApiResponse.ok(storeService.getStoreInfo(storeId));
  }
}
