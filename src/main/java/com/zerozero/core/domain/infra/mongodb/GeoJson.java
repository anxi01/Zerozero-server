package com.zerozero.core.domain.infra.mongodb;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoJson implements org.springframework.data.mongodb.core.geo.GeoJson {

  private String type;
  private Iterable<?> coordinates;

  @Override
  public String getType() {
    return type;
  }

  @Override
  public Iterable<?> getCoordinates() {
    return coordinates;
  }
}
