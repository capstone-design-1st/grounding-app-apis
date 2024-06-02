package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.JwtDto;
import org.example.first.groundingappapis.dto.UserDto;
import org.example.first.groundingappapis.entity.Account;
import org.example.first.groundingappapis.entity.Role;
import org.example.first.groundingappapis.entity.User;
import org.example.first.groundingappapis.exception.UserErrorResult;
import org.example.first.groundingappapis.exception.UserException;
import org.example.first.groundingappapis.repository.AccountRepository;
import org.example.first.groundingappapis.repository.UserRepository;
import org.example.first.groundingappapis.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public boolean userExistsByEmailAndPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                return true; // 인증 성공
            }
        }

        return false; // 인증 실패
    }

    @Override
    @Transactional
    public UserDto.LoginResponseDto login(UserDto.LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        JwtDto.JwtRequestDto jwtRequestDto = JwtDto.JwtRequestDto.builder()
                .email(loginRequestDto.getEmail())
                .userId(user.getId())
                .build();

        String jwt = jwtTokenProvider.createAccessToken(jwtRequestDto);

        return UserDto.LoginResponseDto.builder()
                .userId(user.getId())
                .createdAt(LocalDateTime.now())
                .accessToken(jwt)
                .build();
    }

    @Override
    @Transactional
    public boolean userExistsByName(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto.SignUpResponseDto signUp(UserDto.SignUpRequestDto signUpRequestDto) {
        UUID userId = UUID.randomUUID();
        try {
            User user = User.builder()
                    .id(userId)
                    .email(signUpRequestDto.getEmail())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .name(signUpRequestDto.getName())
                    .phoneNumber(signUpRequestDto.getPhoneNumber())
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .walletAddress(signUpRequestDto.getWalletAddress())
                    .build();
            userRepository.save(user);

            Account account = Account.builder()
                    .deposit(0L)
                    .build();

            accountRepository.save(account);
        } catch (Exception e) {
            log.error("Error creating user: ", e);
        }
        log.info("회원가입 성공");
        return UserDto.SignUpResponseDto.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public boolean userExistsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto.GetEmailResponseDto getEmail(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        UserDto.GetEmailResponseDto getEmailResponseDto = UserDto.GetEmailResponseDto
                .builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
        return  getEmailResponseDto;
    }

    @Override
    public UserDto.GetPhoneNumberResponseDto getPhoneNumber(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        UserDto.GetPhoneNumberResponseDto getPhoneNumberResponseDto = UserDto.GetPhoneNumberResponseDto
                .builder()
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .build();
        return getPhoneNumberResponseDto;
    }

    @Override
    public UserDto.GetWalletAddressResponseDto getWallet(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        UserDto.GetWalletAddressResponseDto getWalletAddressResponseDto = UserDto.GetWalletAddressResponseDto
                .builder()
                .userId(user.getId())
                .walletAddress(user.getWalletAddress())
                .build();
        return getWalletAddressResponseDto;

    }
}
