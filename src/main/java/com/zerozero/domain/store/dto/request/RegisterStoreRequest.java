package com.zerozero.domain.store.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class RegisterStoreRequest {

  private String title;
  private int mapx;
  private int mapy;
  private List<String> images;
}
