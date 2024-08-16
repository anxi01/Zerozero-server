package com.zerozero.external.kakao.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zerozero.core.domain.vo.Image;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordSearchResponse {

  private Meta meta;

  private Document[] documents;

  @Getter
  @Setter
  public static class Meta {

    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("pageable_count")
    private Integer pageableCount;

    @JsonProperty("is_end")
    private Boolean isEnd;

    @JsonProperty("same_name")
    private SameName sameName;

    @Getter
    @Setter
    public static class SameName {

      private String[] region;

      private String keyword;

      @JsonProperty("selected_region")
      private String selectedRegion;
    }
  }

  @Getter
  @Setter
  public static class Document {

    private String id;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("category_group_code")
    private String categoryGroupCode;

    @JsonProperty("category_group_name")
    private String categoryGroupName;

    private String phone;

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("road_address_name")
    private String roadAddressName;

    private String x;

    private String y;

    @JsonProperty("place_url")
    private String placeUrl;

    private String distance;

    private boolean status;

    private UUID storeId;

    private Image[] images;
  }
}
