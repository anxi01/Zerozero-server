package com.zerozero.review;

import com.zerozero.core.domain.vo.ZeroDrink;
import lombok.Getter;

@Getter
public class ReviewRequest {

  private ZeroDrink[] zeroDrinks;
  private String content;
}
