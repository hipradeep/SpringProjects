package com.hipradeep.code.controller;

import com.hipradeep.code.dto.PasswordResetRequestDto;
import com.hipradeep.code.dto.PasswordResetResponseDto;
import com.hipradeep.code.service.TwilioOTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/router")
public class OTPController {

    @Autowired
    private TwilioOTPService twilioOTPService;

    @PostMapping("/sendOTP")
    public Mono<PasswordResetResponseDto> sendOTP(@RequestBody PasswordResetRequestDto passwordResetRequestDto) {
        return twilioOTPService.sendOTP(passwordResetRequestDto);
    }
}
