package com.zerozero.store.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateStoreRequest(
        @NotNull(message = "판매점 이름은 필수 값입니다.")
        @Schema(description = "판매점 이름", example = "꿉당")
        String placeName,

        @NotNull(message = "판매점 x좌표(경도)는 필수 값입니다.")
        @Schema(description = "판매점 x좌표(경도)", example = "127.01275515884753")
        String longitude,

        @NotNull(message = "판매점 y좌표(위도)는 필수 값입니다.")
        @Schema(description = "판매점 y좌표(위도)", example = "37.49206032952165")
        String latitude,

        @NotNull(message = "판매점 사진은 필수 값입니다.")
        @Schema(description = "판매점 업로드 이미지 URL 리스트",
                example = "[\"https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png\", \"https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/another-image.png\"]")
        List<String> images) {
}
