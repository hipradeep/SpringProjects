package com.hipradeep.code.dto;

import lombok.Data;

@Data
public class PasswordResetRequestDto {
    private String phoneNumber; // Destingation phone number ex: +919999999999
    private String userName;
    private String oneTimePassword; // Optional, can be used for verifying or carrying the OTP
}
