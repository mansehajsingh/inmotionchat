package com.inmotionchat.identity.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.inmotionchat.identity.service.contract.FirebaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseWrapperImpl implements FirebaseWrapper {

    @Autowired
    public FirebaseWrapperImpl() {

    }

    @Override
    public UserRecord getUser(String uid) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUser(uid);
    }

    @Override
    public UserRecord create(UserRecord.CreateRequest request) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().createUser(request);
    }

    @Override
    public UserRecord update(UserRecord.UpdateRequest request) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().updateUser(request);
    }

}
