package com.zerozero.core.application;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface BaseRequest {

  @JsonIgnore
  default boolean isValid() {
    return true;
  }
}
