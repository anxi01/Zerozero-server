package com.zerozero.core.domain.infra.mongodb.region;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "region")
public class Region {

  private String type;

  private Properties properties;

  private GeoJson geometry;

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Properties {
    private String adm_nm;
    private String adm_cd;
    private String adm_cd2;
    private String sgg;
    private String sido;
    private String sidonm;
    private String temp;
    private String sggnm;
    private String adm_cd8;
  }
}
