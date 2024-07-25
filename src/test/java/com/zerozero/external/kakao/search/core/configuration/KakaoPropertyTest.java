package com.zerozero.external.kakao.search.core.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.zerozero.external.kakao.search.application.RequestKakaoKeywordSearchUseCase;
import com.zerozero.external.kakao.search.application.RequestKakaoKeywordSearchUseCase.RequestKakaoKeywordSearchRequest;
import com.zerozero.external.kakao.search.dto.KeywordSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class KakaoPropertyTest {

  @Autowired
  RequestKakaoKeywordSearchUseCase requestKakaoKeywordSearchUseCase;

  @Test
  @DisplayName("서울 강남구 삼성동 20km 반경에서 카카오프렌즈 매장 검색한다.")
  void keywordLocalSearch_SearchWithin20KmRadius_KakaoFriendsStoresFound() {

    // given
    RequestKakaoKeywordSearchRequest request = RequestKakaoKeywordSearchRequest.builder()
        .query("카카오프렌즈")
        .latitude("37.514322572335935")
        .longitude("127.06283102249932")
        .radius(20000)
        .build();

    // when
    KeywordSearchResponse response = requestKakaoKeywordSearchUseCase.execute(request).getKeywordSearchResponse();

    // then
    assertThat(response).isNotNull();
    assertThat(response.getMeta()).isNotNull();
    assertThat(response.getMeta().getSameName()).isNotNull();
    assertThat(response.getMeta().getSameName().getKeyword()).isEqualTo("카카오프렌즈");
  }
}