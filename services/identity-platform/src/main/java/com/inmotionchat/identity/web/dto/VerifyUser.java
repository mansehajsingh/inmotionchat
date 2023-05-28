package com.inmotionchat.identity.web.dto;

import java.util.UUID;

public class VerifyUser {

    private String verificationCode;

    public UUID getVerificationCode() {
        return UUID.fromString(verificationCode);
    }

}
