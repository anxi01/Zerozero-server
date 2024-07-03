package com.zerozero.core.domain.vo;

import com.zerozero.core.domain.shared.ValueObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
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
@Schema(description = "제로음료")
public class ZeroDrink extends ValueObject implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "제로음료 종류", example = "COCA_COLA_ZERO")
  private Type type;

  public enum Type {
    COCA_COLA_ZERO,
    PEPSI_ZERO,
    SPRITE_ZERO,
    CHILSUNG_CIDER_ZERO,
    DR_PEPPER_ZERO,
    MILKIS_ZERO,
    TAMS_ZERO_LEMON,
    TAMS_ZERO_ORANGE,
    TAMS_ZERO_APPLE_KIWI,
    TAMS_ZERO_PINEAPPLE,
    TAMS_ZERO_GRAPE,
    TAMS_ZERO_BLUEBERRY_POMEGRANATE,
    WELCHS_ZERO_GRAPE,
    WELCHS_ZERO_ORANGE,
    WELCHS_ZERO_SHINE_MUSCAT,
    FANTA_ZERO_GRAPE,
    FANTA_ZERO_PINEAPPLE
  }
}
