package com.inmotionchat.identity.service.contract;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

public interface FirebaseWrapper {

    UserRecord getUser(String uid) throws FirebaseAuthException;

    UserRecord create(UserRecord.CreateRequest request) throws FirebaseAuthException;

    UserRecord update(UserRecord.UpdateRequest request) throws FirebaseAuthException;

}
