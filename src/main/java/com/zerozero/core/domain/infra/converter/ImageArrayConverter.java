package com.zerozero.core.domain.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerozero.core.domain.vo.Image;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Converter(autoApply = true)
public class ImageArrayConverter implements AttributeConverter<Image[], String> {

  private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Override
  public String convertToDatabaseColumn(Image[] attribute) {
    if (attribute == null) {
      return "[]";
    }
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      log.error("Image[] convert error", e);
      return "[]";
    }
  }

  @Override
  public Image[] convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    try {
      return objectMapper.readValue(dbData, Image[].class);
    } catch (IOException e) {
      log.error("Image[] convert error", e);
      return null;
    }
  }
}
