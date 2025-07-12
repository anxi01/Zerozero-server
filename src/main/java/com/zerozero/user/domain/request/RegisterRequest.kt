package com.zerozero.user.domain.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@JvmRecord
data class RegisterRequest(

    @Schema(description = "닉네임", example = "제로")
    @field:NotBlank(message = "닉네임은 필수 데이터입니다.")
    val nickname: String
)
