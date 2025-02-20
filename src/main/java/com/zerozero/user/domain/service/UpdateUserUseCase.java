package com.zerozero.user.domain.service;

import com.zerozero.user.domain.model.User;
import com.zerozero.user.presentation.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateUserUseCase {

    public void execute(UpdateUserRequest updateUserRequest, User user) {
        String image = updateUserRequest.image();
        user.uploadProfileImage(image);
        user.updateNickname(updateUserRequest.nickname());
    }

}
