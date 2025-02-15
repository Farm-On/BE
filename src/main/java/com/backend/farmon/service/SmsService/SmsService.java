package com.backend.farmon.service.SmsService;

public interface SmsService {
    boolean sendSms(String phoneNumber, boolean isSignup);
    boolean verifyAuthCode(String phoneNumber, String inputCode);
}
