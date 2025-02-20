package com.zerozero.store.domain.service;

import com.zerozero.auth.exception.AuthErrorType;
import com.zerozero.auth.exception.AuthException;
import com.zerozero.core.util.JwtUtil;
import com.zerozero.store.domain.request.StoreLocationRequest;
import com.zerozero.store.domain.response.StoreResponse;
import com.zerozero.store.infrastructure.mongodb.Store;
import com.zerozero.store.infrastructure.mongodb.StoreMongoRepository;
import com.zerozero.user.domain.model.User;
import com.zerozero.user.domain.repository.UserRepository;
import com.zerozero.user.exception.UserErrorType;
import com.zerozero.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class ReadNearbyStoresUseCase {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final StoreMongoRepository storeMongoRepository;

    public static final Double DEFAULT_RADIUS = 1000.0;

    public List<StoreResponse> execute(StoreLocationRequest storeLocationRequest) {
        String accessToken = storeLocationRequest.accessToken();
        if (jwtUtil.isTokenExpired(accessToken)) {
            log.error("[ReadNearbyStoresUseCase] Expired access token");
            throw new AuthException(AuthErrorType.EXPIRED_TOKEN);
        }
        UUID userId = jwtUtil.extractUserId(accessToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorType.NOT_EXIST_USER));
        List<Store> mongoStores = storeMongoRepository.findStoresWithinCoordinatesRadius(storeLocationRequest.longitude(), storeLocationRequest.latitude(), DEFAULT_RADIUS);
        return mongoStores.stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());
    }

}
