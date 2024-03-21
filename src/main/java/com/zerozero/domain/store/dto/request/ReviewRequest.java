package com.zerozero.domain.store.dto.request;

import com.zerozero.domain.store.domain.ZeroDrinks;
import java.util.List;
import lombok.Getter;

@Getter
public class ReviewRequest {

  private List<ZeroDrinks> zeroDrinks;
  private String content;
}
