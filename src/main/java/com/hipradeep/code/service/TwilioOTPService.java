package com.hipradeep.code.service;

import com.hipradeep.code.config.TwilioConfig;
import com.hipradeep.code.dto.OtpStatus;
import com.hipradeep.code.dto.PasswordResetRequestDto;
import com.hipradeep.code.dto.PasswordResetResponseDto;
// import com.twilio.rest.api.v2010.account.Message;
// import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class TwilioOTPService {

    // @Autowired
    // private TwilioConfig twilioConfig;

    // Simulate a database or cache to store OTPs for verification
    Map<String, String> otpMap = new HashMap<>();

    public Mono<PasswordResetResponseDto> sendOTP(PasswordResetRequestDto passwordResetRequestDto) {
        return Mono.just(passwordResetRequestDto)
                .flatMap(dto -> {
                    PasswordResetResponseDto responseDto = null;
                    try {
                        // PhoneNumber to = new PhoneNumber(dto.getPhoneNumber());
                        // PhoneNumber from = new PhoneNumber(twilioConfig.getTrialNumber());
                        String otp = generateOTP();
                        String otpMessage = "Dear Customer , Your OTP is " + otp
                                + ". Use this Passcode to complete your transaction. Thank You.";

                        // --- SIMULATION START ---
                        // In a real scenario, we would make the Twilio API call here.
                        // Since we are simulating, we will just log the OTP to the console.
                        log.info("################################################################");
                        log.info("Sending OTP to: {}", dto.getPhoneNumber());
                        log.info("OTP MESSAGE: {}", otpMessage);
                        log.info("################################################################");

                        // Store the OTP for simulated verification (optional for this demo)
                        otpMap.put(dto.getUserName(), otp);

                        responseDto = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);
                        // --- SIMULATION END ---

                        /*
                         * REAL TWILIO IMPLEMENTATION (Commented Out):
                         * 
                         * com.twilio.Twilio.init(twilioConfig.getAccountSid(),
                         * twilioConfig.getAuthToken());
                         * Message message = Message.creator(
                         * to,
                         * from,
                         * otpMessage)
                         * .create();
                         * 
                         * // You can check message.getStatus() here
                         * if (message.getStatus() == Message.Status.FAILED) {
                         * throw new RuntimeException("Twilio failed to send SMS: " +
                         * message.getErrorMessage());
                         * }
                         * log.info("SMS sent successfully, SID: {}", message.getSid());
                         * responseDto = new PasswordResetResponseDto(OtpStatus.DELIVERED, otpMessage);
                         */

                    } catch (Exception ex) {
                        log.error("Error sending OTP: {}", ex.getMessage());
                        responseDto = new PasswordResetResponseDto(OtpStatus.FAILED, ex.getMessage());
                    }
                    return Mono.just(responseDto);
                });
    }

    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
}
