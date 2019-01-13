package com.bora.gustavo.helper;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

public class Utils {
    private static final String TAG = "Utils";
    public static final String PARAM_GYM = "gym";

    public String getUserUid() throws NullPointerException {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser == null ? null : currentUser.getUid();
    }

    public static String createUuid() {
        return UUID.randomUUID().toString();
    }

    public static void showSnackbar(View parentLayout, int closeText, int message) {
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                .setAction(closeText, view -> Log.d(TAG, "Snackbar clicked!"))
                //.setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
}
