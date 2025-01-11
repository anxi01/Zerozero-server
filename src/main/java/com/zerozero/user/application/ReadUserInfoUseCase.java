package com.zerozero.user.application;

import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.application.BaseResponse;
import com.zerozero.core.application.BaseUseCase;
import com.zerozero.core.domain.entity.User;
import com.zerozero.core.domain.infra.repository.StoreJPARepository;
import com.zerozero.core.domain.vo.Image;
import com.zerozero.core.exception.DomainException;
import com.zerozero.core.exception.error.BaseErrorCode;
import com.zerozero.user.application.ReadUserInfoUseCase.ReadUserInfoRequest;
import com.zerozero.user.application.ReadUserInfoUseCase.ReadUserInfoResponse;
import com.zerozero.user.application.ReadUserInfoUseCase.ReadUserInfoResponse.UserProfile;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadUserInfoUseCase implements BaseUseCase<ReadUserInfoRequest, ReadUserInfoResponse> {

  private final StoreJPARepository storeJPARepository;

  @Override
  public ReadUserInfoResponse execute(ReadUserInfoRequest request) {
    if (request == null || !request.isValid()) {
      log.error("[ReadUserInfoUseCase] Invalid request");
      return ReadUserInfoResponse.builder().success(false)
          .errorCode(ReadUserInfoErrorCode.NOT_EXIST_REQUEST_CONDITION).build();
    }
    User user = request.getUser();
    Integer storeReportCount = storeJPARepository.countStoresByUserId(user.getId());
    return ReadUserInfoResponse.builder().userProfile(UserProfile.builder()
            .nickname(user.getNickname())
            .profileImage(user.getProfileImage())
            .rank(calculateUserRank(user))
            .storeReportCount(Optional.ofNullable(storeReportCount).orElse(0))
            .build())
        .build();
  }

  private Integer calculateUserRank(User user) {
    if (user == null) {
      return null;
    }
    List<Object[]> userRankings = storeJPARepository.findUserIdWithStoreCountOrderByStoreCountDesc();
    return IntStream.range(0, userRankings.size())
        .filter(i -> userRankings.get(i)[0].equals(user.getId()))
        .mapToObj(i -> i + 1)
        .findFirst().orElse(null);
  }

  @Getter
  @RequiredArgsConstructor
  public enum ReadUserInfoErrorCode implements BaseErrorCode<DomainException> {
    NOT_EXIST_REQUEST_CONDITION(HttpStatus.BAD_REQUEST, "요청 조건이 올바르지 않습니다."),
    ;

    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public DomainException toException() {
      return new DomainException(httpStatus, this);
    }
  }

  @ToString
  @Getter
  @Setter
  @SuperBuilder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadUserInfoResponse extends BaseResponse<ReadUserInfoErrorCode> {

    private UserProfile userProfile;

    @ToString
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class UserProfile {

      private String nickname;

      private Image profileImage;

      private Integer rank;

      private Integer storeReportCount;
    }
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PROTECTED)
  public static class ReadUserInfoRequest implements BaseRequest {

    private User user;

    @Override
    public boolean isValid() {
      return user != null;
    }
  }
}
