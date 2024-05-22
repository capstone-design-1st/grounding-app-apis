package org.example.first.groundingappapis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.first.groundingappapis.entity.User;
import org.example.first.groundingappapis.exception.UserErrorResult;
import org.example.first.groundingappapis.security.UserPrincipal;
import org.example.first.groundingappapis.service.MailService;
import org.example.first.groundingappapis.service.SmsService;
import org.example.first.groundingappapis.service.UserService;
import org.example.first.groundingappapis.service.VerificationService;
import org.example.first.groundingappapis.dto.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SmsService smsService;
    private final VerificationService verificationService;
    private final MailService mailService;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Valid @RequestBody UserDto.LoginRequestDto loginRequestDto) {

        if (loginRequestDto.getEmail() == null || loginRequestDto.getPassword() == null) {
            log.info("필수값 누락");
            UserErrorResult userErrorResult = UserErrorResult.REQUIRED_VALUE;
            ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();

            return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
        }
        if (!userService.userExistsByEmail(loginRequestDto.getEmail())) {
            log.info("존재하지 않는 사용자");
            UserErrorResult userErrorResult = UserErrorResult.USER_NOT_FOUND;
            ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();
            return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
        }
        if (!userService.userExistsByEmailAndPassword(loginRequestDto.getEmail(), loginRequestDto.getPassword())) {
            log.info("비밀번호 불일치");
            UserErrorResult userErrorResult = UserErrorResult.USER_NOT_FOUND;
            ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();

            return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
        }


        UserDto.LoginResponseDto loginResponseDto = userService.login(loginRequestDto);

        log.info("로그인 성공");

        ResponseDto responseDto = ResponseDto.builder()
                .payload(objectMapper.convertValue(loginResponseDto, Map.class))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto); //200
    }
    @PostMapping
    public ResponseEntity<ResponseDto> signUp(@Valid @RequestBody UserDto.
            SignUpRequestDto signUpRequestDto) throws Exception {
        try{
            if (signUpRequestDto.getEmail() == null || signUpRequestDto.getPassword() == null || signUpRequestDto.getNickname() == null || signUpRequestDto.getPhoneNumber() == null) {
                log.info("필수값 누락");
                UserErrorResult userErrorResult = UserErrorResult.REQUIRED_VALUE;
                ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();

                return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
            }

            if (userService.userExistsByNickname(signUpRequestDto.getEmail())) {
                log.info("이미 존재하는 닉네임");
                UserErrorResult userErrorResult = UserErrorResult.DUPLICATED_NICKNAME;
                ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();

                return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
            }

//            if (!verificationService.isVerified(signUpRequestDto.getPhoneNumber())){
//                log.info("검증되지 않은 전화번호");
//                UserErrorResult userErrorResult = UserErrorResult.UNVERIFIED_PHONE_NUMBER;
//                ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();
//
//                return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
//            }


            if (userService.userExistsByEmail(signUpRequestDto.getEmail())) {
                log.info("이미 존재하는 이메일");
                UserErrorResult userErrorResult = UserErrorResult.DUPLICATED_EMAIL;

                ResponseDto responseDto = ResponseDto.builder()
                        .error(userErrorResult.getMessage())
                        .build();

                return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
            }

            UserDto.SignUpResponseDto signUpResponseDto = userService.signUp(signUpRequestDto);

            ResponseDto responseDto = ResponseDto.builder()
                    .payload(objectMapper.convertValue(signUpResponseDto, Map.class))
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto); //201


        }catch(Exception e){
            log.info("회원가입 실패");
            ResponseDto responseDto = ResponseDto.builder()
                    .error("회원가입 실패")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto); //400
        }
    }
    @PostMapping("/sign-up/email/validation")
    public ResponseEntity<ResponseDto> checkDuplicatedEmail(@Valid @RequestBody UserDto.CheckDuplicatedEmailRequestDto checkDuplicatedEmailRequestDto) {

        String email = checkDuplicatedEmailRequestDto.getEmail();
        if (userService.userExistsByEmail(email)) {
            log.info("이메일 중복");
            UserErrorResult userErrorResult = UserErrorResult.DUPLICATED_EMAIL;

            ResponseDto responseDto = ResponseDto.builder()
                    .error(userErrorResult.getMessage())
                    .build();

            return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto); //409
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //204
    }

    @PostMapping("/sign-up/phone/validation")
    public ResponseEntity<ResponseDto> validationPhoneNumber(@Valid @RequestBody SmsDto.ValidationRequestDto validationRequestDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String phoneNumber = validationRequestDto.getPhoneNumber();

        if (userService.userExistsByPhoneNumber(phoneNumber)) {
            log.info("이미 존재하는 전화번호");
            UserErrorResult userErrorResult = UserErrorResult.DUPLICATED_PHONE_NUMBER;
            ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();

            return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
        }

        String verificationCode = verificationService.makeVerificationCode();

        SmsDto.MessageDto messageDto = SmsDto.MessageDto.builder()
                .to(phoneNumber)
                .content("[그라운딩]\n가입 인증번호는 " + verificationCode + " 입니다")
                .build();

        log.info(verificationCode);

        smsService.sendAlimtalk(messageDto);

        verificationService.saveVerificationCode(phoneNumber, verificationCode);
        verificationService.saveCompletionCode(phoneNumber, false);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //204
    }
    @PostMapping("/sign-up/phone/verification")
    public ResponseEntity<ResponseDto> verificationPhoneNumber(@Valid @RequestBody SmsDto.VerificationRequestDto verificationRequestDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String phoneNumber = verificationRequestDto.getPhoneNumber();

        if (verificationService.verifyCode(verificationRequestDto.getPhoneNumber(), verificationRequestDto.getVerificationCode())) {
            log.info("인증 코드 검증 성공");

            verificationService.deleteVerificationCode(verificationRequestDto.getPhoneNumber());
            verificationService.saveCompletionCode(verificationRequestDto.getPhoneNumber(), true);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //204
        } else {
            log.info("인증 코드 검증 실패");
            UserErrorResult userErrorResult = UserErrorResult.FAILED_VALIDATING_CODE;
            ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();

            return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
        }
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping //API-102
    public ResponseEntity<ResponseDto> withdrawUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        final User user = userPrincipal.getUser();

        UUID userId = user.getId();

        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //204
    }

    @GetMapping("/{userId}/phone-number")
    public ResponseEntity<ResponseDto> getPhoneNumberWithUserId(@PathVariable UUID userId) throws Exception {

        UserDto.GetPhoneNumberResponseDto getPhoneNumberResponseDto = userService.getPhoneNumber(userId);

        ResponseDto responseDto = ResponseDto.builder()
                .payload(objectMapper.convertValue(getPhoneNumberResponseDto, Map.class))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto); //200
    }

    @GetMapping("/{userId}/email")
    public ResponseEntity<ResponseDto> getEmailWithUserId(@PathVariable UUID userId) throws Exception {
        UserDto.GetEmailResponseDto getEmailResponseDto = userService.getEmail(userId);

        ResponseDto responseDto = ResponseDto.builder()
                .payload(objectMapper.convertValue(getEmailResponseDto, Map.class))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto); //200
    }

    //email validation, verification
    @PostMapping("/email/validation")
    public ResponseEntity<ResponseDto> validationEmail(@Valid @RequestBody MailDto.MailRequestDto mailRequestDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String email = mailRequestDto.getEmail();

        if (!userService.userExistsByEmail(email)) {
            log.info("존재하지 않는 회원");
            UserErrorResult userErrorResult = UserErrorResult.USER_NOT_FOUND;
            ResponseDto responseDto = ResponseDto.builder()
                    .error(userErrorResult.getMessage())
                    .build();

            return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
        }

        String verificationCode = verificationService.makeVerificationCode();

        MailDto.MailSendDto mailSendDto = MailDto.MailSendDto.builder()
                .email(email)
                .title("그라운딩 이메일 인증코드입니다.")
                .content("인증번호는 [" + verificationCode + "]입니다.")
                .build();
        mailService.sendMail(mailSendDto);

        verificationService.saveVerificationCode(email, verificationCode);
        verificationService.saveCompletionCode(email, false);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //204
    }
    @PostMapping("/email/verification")
    public ResponseEntity<ResponseDto> verificationEmail(@RequestBody MailDto.MailVerifyDto mailVerifyDto) {
        String email = mailVerifyDto.getEmail();

        if (verificationService.verifyCode(email, mailVerifyDto.getVerificationCode())) {
            log.info("인증 코드 검증 성공");
            //검증 성공 후 코드 삭제, 완료 코드 생성
            verificationService.deleteVerificationCode(email);
            verificationService.saveCompletionCode(email, true);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //204
        } else {
            log.info("인증 코드 검증 실패");
            UserErrorResult userErrorResult = UserErrorResult.FAILED_VALIDATING_CODE;
            ResponseDto responseDto = ResponseDto.builder().error(userErrorResult.getMessage()).build();

            return ResponseEntity.status(userErrorResult.getHttpStatus()).body(responseDto);
        }
    }
//
//    //현재 투자금액
//    @GetMapping("/investment")
//    public ResponseEntity<ResponseDto> getUserDeposit(@AuthenticationPrincipal User user) throws Exception{
//        /*
//
//         */
//        return null;
//    }
//    //사용자 보유 자산
//    @GetMapping("/properties")
//    public ResponseEntity<ResponseDto> getUserProperties(@AuthenticationPrincipal User user) throws Exception {
//        /*
//
//         */
//        return null;
//    }
//
//    //사용자 보유 예수금 조회
//    @GetMapping("/deposit")
//    public ResponseEntity<ResponseDto> getUserDeposit(@AuthenticationPrincipal User user) throws Exception {
//        /*
//
//         */
//        return null;
//    }
//
//    //POST: 입금
//    @PostMapping("/deposit")
//    public ResponseEntity<ResponseDto> deposit(@AuthenticationPrincipal User user, @RequestBody UserDto.DepositRequestDto depositRequestDto) throws Exception {
//        /*
//
//         */
//        return null;
//    }
//    //POST: 출금
//    @PostMapping("/withdraw")
//    public ResponseEntity<ResponseDto> withdraw(@AuthenticationPrincipal User user, @RequestBody UserDto.WithdrawRequestDto withdrawRequestDto) throws Exception {
//        /*
//
//         */
//        return null;
//    }
//
//    //GET: 입출금 로그 조회
//    @GetMapping("/{userId}/deposit-withdraw-logs")
//    public ResponseEntity<ResponseDto> getDepositWithdrawLogs(@AuthenticationPrincipal User user) throws Exception {
//        /*
//
//         */
//        return null;
//    }
//
//    //GET: 거래 내역 조회, 기간, 매수/매도, 당일/1개월/3개월/6개월, 부동산/건물
//    @GetMapping("/trade-logs")
//    public ResponseEntity<ResponseDto> getTradeLogs(@AuthenticationPrincipal User user, @RequestBody UserDto.TradeLogRequestDto tradeLogRequestDto) throws Exception {
//        /*
//
//         */
//        return null;
//    }

}
