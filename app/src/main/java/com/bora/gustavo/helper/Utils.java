package com.bora.gustavo.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

public class Utils {
    private static final String TAG = "Utils";
    public static final String PARAM_GYM = "gym";

    public static String getUserUid() throws NullPointerException {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser == null ? null : currentUser.getUid();
    }

    public static String createUuid() {
        return UUID.randomUUID().toString();
    }
}
