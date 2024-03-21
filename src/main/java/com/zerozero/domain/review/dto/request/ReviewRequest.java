package com.zerozero.domain.review.dto.request;

import com.zerozero.domain.review.domain.ZeroDrinks;
import java.util.List;
import lombok.Getter;

@Getter
public class ReviewRequest {

  private List<ZeroDrinks> zeroDrinks;
  private String content;
}
