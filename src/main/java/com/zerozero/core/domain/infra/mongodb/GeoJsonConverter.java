package com.zerozero.core.domain.infra.mongodb;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Log4j2
public class GeoJsonConverter {

  public static GeoJsonPoint of(String longitude, String latitude) {
    if (longitude == null || latitude == null) {
      return null;
    }
    try {
      double x = Double.parseDouble(longitude);
      double y = Double.parseDouble(latitude);
      return new GeoJsonPoint(x, y);
    } catch (NumberFormatException e) {
      log.error(e);
      return null;
    }
  }
}
