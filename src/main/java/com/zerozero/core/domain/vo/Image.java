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
@Schema(description = "이미지")
public class Image extends ValueObject implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "이미지 URL", example = "https://s3.ap-northeast-2.amazonaws.com/zerozero-upload/images/f98da6af-d78b-43da-afb9-83ca8c762167.png")
  private String url;

  public static Image convertUrlToImage(String url) {
    return Image.builder().url(url).build();
  }
}
