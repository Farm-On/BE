package com.backend.farmon.service.SmsService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.domain.SmsAuth;
import com.backend.farmon.repository.SmsAuthRepository.SmsAuthRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsAuthRepository smsAuthRepository;
    private final UserRepository userRepository;
    private DefaultMessageService messageService;

    @Value("${spring.sms.api-key}")
    private String apiKey;
    @Value("${spring.sms.api-secret}")
    private String apiSecret;
    @Value("${spring.sms.provider}")
    private String smsProvider; // 수신자
    @Value("${spring.sms.sender}")
    private String smsSender; // 발신자

    @PostConstruct
    public void init(){
        messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, smsProvider);
    }

    // 인증 코드 생성
    @Override
    @Transactional
    public boolean sendSms(String phoneNum) {
        // 가입한 번호가 이미 존재하는지 검증
        if (userRepository.existsByPhoneNum(phoneNum)) {
            throw new UserHandler(ErrorStatus.PHONENUM_ALREADY_EXIST);  // 전화번호가 이미 존재할 경우 예외 발생
        }
        // 기존 인증있으면(인증문자 재발급일 경우) 해당 엔티티 삭제
        smsAuthRepository.findByPhoneNum(phoneNum)
                .ifPresent(smsAuthRepository::delete);

        // 전송할 message객체 생성
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(smsSender);
        message.setTo(phoneNum);

        // 새 인증 코드 생성
        String certificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000); // 6자리 인증 코드를 랜덤으로 생성

        // 문자인증 엔티티 생성
        SmsAuth newSmsAuth = SmsAuth.builder()
                .phoneNum(phoneNum)
                .authCode(certificationCode)
                .expirationTime(LocalDateTime.now().plusMinutes(3)) // 3분 후 만료
                .build();
        smsAuthRepository.save(newSmsAuth);

        message.setText("FarmOn 본인확인 인증번호는 " + certificationCode + "입니다.");

        // 문자 전송
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

        String statusCode = response.getStatusCode();
        boolean result = statusCode.equals("2000"); // 전송성공시 statusCode는 2000. 다른 값이 담길시 result는 false가 됨
        return result;
    }

    // 인증 코드 검증
    @Override
    @Transactional
    public boolean verifyAuthCode(String phoneNumber, String inputCode) {
        // 입력 번호에 해당하는 인증 코드 엔티티 가져옴
        Optional<SmsAuth> optionalSmsAuth = smsAuthRepository.findByPhoneNum(phoneNumber);

        // 해당 번호에 해당하는 엔티티가 존재하지 않으면 에러발생
        if (optionalSmsAuth.isEmpty()) {
            throw new UserHandler(ErrorStatus.PHONENUM_NOT_EXIST);
        }
        // 해당 번호로 생성된 인증 번호 엔티티가 존재한다면(아직 인증번호 일치하는지 검사는 안함), 해당 인증 코드 객체를 가져옴
        SmsAuth smsAuth = optionalSmsAuth.get();

        // 인증 코드의 만료 시간이 현재 시간보다 이전인지 확인
        if (smsAuth.getExpirationTime().isBefore(LocalDateTime.now())) {
            smsAuthRepository.delete(smsAuth); // 만료된 객체 삭제
            throw new UserHandler(ErrorStatus.AUTHCODE_INVALID);
        }

        // 입력한 인증 코드와 데이터베이스에서 가져온 인증 코드가 일치하는지 비교
        return smsAuth.getAuthCode().equals(inputCode);
    }
}