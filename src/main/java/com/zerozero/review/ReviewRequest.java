package com.zerozero.review;

import java.util.List;
import lombok.Getter;

@Getter
public class ReviewRequest {

  private List<ZeroDrinks> zeroDrinks;
  private String content;
}
