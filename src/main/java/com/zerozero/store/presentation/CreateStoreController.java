package com.zerozero.store.presentation;

import com.zerozero.configuration.argumentresolver.LoginUser;
import com.zerozero.configuration.interceptor.Authorization;
import com.zerozero.configuration.property.CreateStoreQueueProperty;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.exception.error.GlobalErrorCode;
import com.zerozero.queue.store.CreateStoreMessageProducer;
import com.zerozero.queue.store.CreateStoreMessageProducer.CreateStoreMessageProducerRequest;
import com.zerozero.store.application.CreateStoreUseCase;
import com.zerozero.store.application.CreateStoreUseCase.CreateStoreErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Store", description = "판매점")
public class CreateStoreController {

  private final CreateStoreUseCase createStoreUseCase;

  private final CreateStoreQueueProperty createStoreQueueProperty;

  @Operation(
      summary = "판매점 등록 API",
      description = "사용자가 검색한 판매점 ID를 통해 제로음료 판매점을 등록합니다.",
      operationId = "/store"
  )
  @ApiErrorCode({GlobalErrorCode.class, CreateStoreErrorCode.class})
  @Authorization
  @PostMapping("/store")
  public ResponseEntity<CreateStoreResponse> createStore(@Valid @RequestBody CreateStoreRequest request,
                                                         @Parameter(hidden = true) @LoginUser User user) {
    CreateStoreUseCase.CreateStoreResponse createStoreResponse = createStoreUseCase.execute(
        CreateStoreUseCase.CreateStoreRequest.builder()
            .placeName(request.getPlaceName())
            .longitude(request.getLongitude())
            .latitude(request.getLatitude())
            .images(request.getImages())
            .user(user)
            .build());
    if (createStoreResponse == null || !createStoreResponse.isSuccess()) {
      Optional.ofNullable(createStoreResponse)
          .map(BaseResponse::getErrorCode)
          .ifPresentOrElse(errorCode -> {
            throw errorCode.toException();
          }, () -> {
            throw GlobalErrorCode.INTERNAL_ERROR.toException();
          });
    }

    CreateStoreMessageProducer createStoreMessageProducer = new CreateStoreMessageProducer(createStoreQueueProperty,
        CreateStoreMessageProducerRequest.builder()
            .storeId(createStoreResponse.getStoreId())
            .build());
    createStoreMessageProducer.publishMessage();

    return ResponseEntity.ok(
        CreateStoreResponse.builder()
            .storeId(createStoreResponse.getStoreId())
            .build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "판매점 등록 응답")
  public static class CreateStoreResponse extends BaseResponse<GlobalErrorCode> {

    @Schema(description = "판매점 ID", example = "11ef3e05-f45b-7e6c-a084-7b554bfaa162")
    private UUID storeId;
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "판매점 등록 요청")
  public static class CreateStoreRequest implements BaseRequest {

    @NotNull(message = "판매점 이름은 필수 값입니다.")
    @Schema(description = "판매점 이름", example = "꿉당")
    private String placeName;

    @NotNull(message = "판매점 x좌표(경도)는 필수 값입니다.")
    @Schema(description = "판매점 x좌표(경도)", example = "127.01275515884753")
    private String longitude;

    @NotNull(message = "판매점 y좌표(위도)는 필수 값입니다.")
    @Schema(description = "판매점 y좌표(위도)", example = "37.49206032952165")
    private String latitude;

    @NotNull(message = "판매점 사진은 필수 값입니다.")
    @Schema(description = "판매점 업로드 이미지 URL 리스트",
        example = "[\"https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png\", \"https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/another-image.png\"]")
    private List<String> images;
  }
}
