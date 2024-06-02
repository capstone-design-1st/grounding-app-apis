package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.UserDto;
import org.example.first.groundingappapis.entity.User;

import java.util.UUID;

public interface UserService {
    boolean userExistsByEmail(String email);

    boolean userExistsByEmailAndPassword(String email, String password);

    UserDto.LoginResponseDto login(UserDto.LoginRequestDto loginRequestDto);

    boolean userExistsByName(String email);

    UserDto.SignUpResponseDto signUp(UserDto.SignUpRequestDto signUpRequestDto);

    boolean userExistsByPhoneNumber(String phoneNumber);

    void deleteUser(UUID userId);

    UserDto.GetEmailResponseDto getEmail(UUID userId);

    UserDto.GetPhoneNumberResponseDto getPhoneNumber(UUID userId);

    UserDto.GetWalletAddressResponseDto getWallet(UUID userId);
}
