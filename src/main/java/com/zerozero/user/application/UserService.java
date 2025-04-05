package com.zerozero.user.application;

import com.zerozero.store.domain.response.StoreUserRankProjection;
import com.zerozero.store.domain.service.GetStoreUserRankUseCase;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.presentation.response.ReadUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final GetStoreUserRankUseCase getStoreUserRankUseCase;

    public ReadUserInfoResponse readUserInfo(User user) {
        StoreUserRankProjection storeUserRankProjection = getStoreUserRankUseCase.execute(user.getId());
        return ReadUserInfoResponse.of(user, storeUserRankProjection);
    }

}
