package com.inmotionchat.identity.firebase;

import com.google.firebase.auth.AuthErrorCode;
import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;

public class FirebaseErrorCodeTranslator {

    private static FirebaseErrorCodeTranslator instance;

    private FirebaseErrorCodeTranslator() {}

    public static FirebaseErrorCodeTranslator getInstance() {
        if (instance != null) return instance;
        instance = new FirebaseErrorCodeTranslator();
        return instance;
    }

    public void translateAuthErrorCode(AuthErrorCode errorCode) throws ConflictException, NotFoundException {
        switch (errorCode) {
            case EMAIL_ALREADY_EXISTS:
                throw new ConflictException(
                    LogicalConstraints.User.EMAIL_EXISTS, "A user with this email already exists.");
            case USER_NOT_FOUND:
                throw new NotFoundException("No unverified user with the provided uid exists.");
            default:
                break;
        }
    }

}
