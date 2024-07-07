package com.zerozero.user.presentation;

import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.core.domain.vo.Store;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.user.application.ReadUserStoresUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자")
public class ReadUserStoresController {

  private final ReadUserStoresUseCase readUserStoresUseCase;

  @Operation(
      summary = "사용자가 등록한 판매점 목록 조회 API",
      description = "사용자가 등록한 판매점 목록을 조회합니다.",
      operationId = "/user/stores"
  )
  @GetMapping("/user/stores")
  public ResponseEntity<ReadUserStoresResponse> readUserStores(@Parameter(hidden = true) AccessToken accessToken) {
    ReadUserStoresUseCase.ReadUserStoresResponse readUserStoresResponse = readUserStoresUseCase.execute(
        ReadUserStoresUseCase.ReadUserStoresRequest.builder()
            .accessToken(accessToken)
            .build());
    if (readUserStoresResponse == null || !readUserStoresResponse.isSuccess()) {
      Optional.ofNullable(readUserStoresResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }
    return ResponseEntity.ok(
        ReadUserStoresResponse.builder().stores(readUserStoresResponse.getStores()).build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "사용자 등록 판매점 목록 조회 응답")
  public static class ReadUserStoresResponse extends BaseResponse<GlobalErrorCode> {

    @Schema(description = "판매점 목록")
    private List<Store> stores;
  }

}
