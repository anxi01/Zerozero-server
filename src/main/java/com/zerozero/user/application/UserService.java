package com.zerozero.user.application;

import com.zerozero.image.domain.model.Image;
import com.zerozero.store.domain.response.StoreUserRankResponse;
import com.zerozero.store.domain.service.GetStoreUserRankUseCase;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.presentation.response.ReadUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final GetStoreUserRankUseCase getStoreUserRankUseCase;

    public ReadUserInfoResponse readUserInfo(User user) {
        StoreUserRankResponse storeUserRankResponse = getStoreUserRankUseCase.execute(user.getId());
        return ReadUserInfoResponse.builder()
                .nickname(user.getNickname())
                .profileImage(Optional.ofNullable(user.getProfileImage()).map(Image::getImageUrl).orElse(null))
                .rank(storeUserRankResponse.rank())
                .storeReportCount(storeUserRankResponse.storeReportCount())
                .build();
    }

}
