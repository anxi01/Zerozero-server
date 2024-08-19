package com.zerozero.core.domain.vo;

import com.zerozero.core.domain.shared.ValueObject;
import com.zerozero.core.domain.vo.ZeroDrink.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "리뷰")
public class Review extends ValueObject implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "리뷰 ID", example = "8e3006f4-3a16-11ef-9454-0242ac120002")
  private UUID id;

  @Schema(description = "리뷰 내용", example = "제로 음료 판매중!")
  private String content;

  @Schema(description = "제로 음료수 목록", example = "[\"COCA_COLA_ZERO\", \"PEPSI_ZERO\", \"SPRITE_ZERO\"]")
  private List<Type> zeroDrinks;

  @Schema(description = "작성한 사용자 ID")
  private UUID userId;

  public static Review of(com.zerozero.core.domain.entity.Review review) {
    if (review == null) {
      return null;
    }
    return Review.builder()
        .id(review.getId())
        .content(review.getContent())
        .zeroDrinks(Optional.ofNullable(review.getZeroDrinks())
            .map(zeroDrinks -> Arrays.stream(zeroDrinks)
                .map(ZeroDrink::getType)
                .collect(Collectors.toList()))
            .orElse(null))
        .userId(review.getUserId())
        .build();
  }
}
