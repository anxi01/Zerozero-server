package com.zerozero.store.presentation.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

@JvmRecord
data class CreateStoreRequest(

    @Schema(description = "카카오 ID", example = "25770215")
    @field:NotBlank(message = "판매점 카카오 ID는 필수 값입니다.")
    val kakaoId: String,

    @Schema(description = "판매점 이름", example = "꿉당")
    @field:NotBlank(message = "판매점 이름은 필수 값입니다.")
    val placeName: String,

    @Schema(description = "판매점 카테고리", example = "음식점 > 한식 > 육류,고기")
    @field:NotBlank(message = "판매점 카테고리는 필수 값입니다.")
    val category: String,

    @Schema(description = "판매점 전화번호", example = "02-525-6692")
    @field:NotBlank(message = "판매점 전화번호는 필수 값입니다.")
    val phone: String,

    @Schema(description = "판매점 주소", example = "서울특별시 강남구 역삼동 123-4")
    @field:NotBlank(message = "판매점 주소는 필수 값입니다.")
    val address: String,

    @Schema(description = "판매점 도로명 주소", example = "서울특별시 강남구 테헤란로 123")
    @field:NotBlank(message = "판매점 도로명 주소는 필수 값입니다.")
    val roadAddress: String,

    @Schema(description = "판매점 x좌표(경도)", example = "127.01275515884753")
    @field:NotBlank(message = "판매점 x좌표(경도)는 필수 값입니다.")
    val longitude: String,

    @Schema(description = "판매점 y좌표(위도)", example = "37.49206032952165")
    @field:NotBlank(message = "판매점 y좌표(위도)는 필수 값입니다.")
    val latitude: String,

    @Schema(description = "판매점 상세페이지 URL", example = "http://place.map.kakao.com/26338954")
    @field:NotBlank(message = "판매점 상세페이지 URL은 필수 값입니다.")
    val placeUrl: String,

    @Schema(
        description = "판매점 업로드 이미지 URL 리스트",
        example = "[\"https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/0cbf3b99-b0ba-4148-a891-d04cb71ae236-test.png\", \"https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/store/another-image.png\"]"
    )
    @field:NotEmpty(message = "판매점 사진은 필수 값입니다.")
    val images: MutableList<String>
)
