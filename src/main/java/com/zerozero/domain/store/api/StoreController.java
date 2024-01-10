package com.zerozero.domain.store.api;

import com.zerozero.domain.store.application.StoreService;
import com.zerozero.domain.store.dto.request.RegisterStoreRequest;
import com.zerozero.domain.store.dto.response.StoreInfoResponse;
import com.zerozero.domain.store.dto.response.StoreListResponse;
import com.zerozero.global.common.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Store Controller", description = "판매점 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

  private final StoreService storeService;

  @Operation(summary = "판매점 검색", description = "사용자가 검색한 판매점 리스트를 보여줍니다.")
  @GetMapping("/search")
  public ApiResponse<StoreListResponse> getStores(@RequestParam String query) {
    return ApiResponse.ok(storeService.search(query));
  }

  @Operation(summary = "판매점 등록", description = "사용자가 검색한 판매점 리스트를 통해 제로음료수 판매점을 등록합니다.")
  @PostMapping
  public ApiResponse<StoreInfoResponse> addStore(Principal connectedUser,
      @RequestParam String query,
      @RequestBody RegisterStoreRequest request) {
    return ApiResponse.ok(storeService.add(connectedUser, query, request));
  }

  @Operation(summary = "판매점 조회", description = "각각의 판매점 정보를 조회합니다.")
  @GetMapping("/{storeId}")
  public ApiResponse<StoreInfoResponse> getStoreInfo(@PathVariable Long storeId) {
    return ApiResponse.ok(storeService.getStoreInfo(storeId));
  }
}
