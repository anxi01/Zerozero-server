package com.zerozero.core.domain.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerozero.core.domain.vo.ZeroDrink;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Converter(autoApply = true)
public class ZeroDrinkArrayConverter implements AttributeConverter<ZeroDrink[], String> {

  private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Override
  public String convertToDatabaseColumn(ZeroDrink[] attribute) {
    if (attribute == null) {
      return "[]";
    }
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      log.error("ZeroDrink[] convert error", e);
      return "[]";
    }
  }

  @Override
  public ZeroDrink[] convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    try {
      return objectMapper.readValue(dbData, ZeroDrink[].class);
    } catch (IOException e) {
      log.error("ZeroDrink[] convert error", e);
      return null;
    }
  }
}
