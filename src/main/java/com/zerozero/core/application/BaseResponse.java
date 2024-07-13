package com.zerozero.core.application;

import com.zerozero.core.exception.error.BaseErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "기본 응답")
public class BaseResponse<E extends BaseErrorCode<?>> {

  @Builder.Default
  @Schema(description = "성공 여부", example = "true")
  private boolean success = true;

  @Schema(description = "에러 코드", hidden = true)
  private E errorCode;
}
