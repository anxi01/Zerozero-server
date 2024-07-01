package com.zerozero.external.naver;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLocalResponse {

  private int total;
  private int start;
  private int display;
  private List<SearchLocalItem> items;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SearchLocalItem {

    private String title;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String roadAddress;
    private int mapx;
    private int mapy;
    private boolean selling;
    private long storeId;

    public String getTitle() {
      return deleteHtmlTags(this.title);
    }

    public void setSellingTrue() {
      this.selling = true;
    }
  }

  private static String deleteHtmlTags(String name) {
    return name.replaceAll("\\<.*?\\>", "");
  }
}
