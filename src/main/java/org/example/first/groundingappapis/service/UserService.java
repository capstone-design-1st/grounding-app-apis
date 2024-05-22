package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.UserDto;

import java.util.UUID;

public interface UserService {
    boolean userExistsByEmail(String email);

    boolean userExistsByEmailAndPassword(String email, String password);

    UserDto.LoginResponseDto login(UserDto.LoginRequestDto loginRequestDto);

    boolean userExistsByNickname(String email);

    UserDto.SignUpResponseDto signUp(UserDto.SignUpRequestDto signUpRequestDto);

    boolean userExistsByPhoneNumber(String phoneNumber);

    void deleteUser(UUID userId);

    UserDto.GetEmailResponseDto getEmail(UUID userId);

    UserDto.GetPhoneNumberResponseDto getPhoneNumber(UUID userId);
}
