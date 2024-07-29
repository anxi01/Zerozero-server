package com.zerozero.store.presentation;

import com.zerozero.configuration.property.CreateStoreQueueProperty;
import com.zerozero.configuration.swagger.ApiErrorCode;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.domain.vo.AccessToken;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
  @PostMapping(value = "/store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CreateStoreResponse> createStore(@Valid @ParameterObject CreateStoreRequest request,
      @RequestPart @Parameter(description = "이미지 원본 파일") List<MultipartFile> imageFiles,
      @Parameter(hidden = true) AccessToken accessToken) {
    CreateStoreUseCase.CreateStoreResponse createStoreResponse = createStoreUseCase.execute(
        CreateStoreUseCase.CreateStoreRequest.builder()
            .placeName(request.getPlaceName())
            .longitude(request.getLongitude())
            .latitude(request.getLatitude())
            .imageFiles(imageFiles)
            .accessToken(accessToken)
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

    return ResponseEntity.ok(CreateStoreResponse.builder().build());
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @Schema(description = "판매점 등록 응답")
  public static class CreateStoreResponse extends BaseResponse<GlobalErrorCode> {
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
  }
}
