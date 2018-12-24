package com.bora.gustavo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bora.gustavo.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BackActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    @BindView(R.id.sign_up_name)
    EditText mInputName;
    @BindView(R.id.sign_up_email)
    EditText mInputEmail;
    @BindView(R.id.sign_up_password)
    EditText mInputPassword;
    @BindView(R.id.sign_up_progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.sign_up_sign_in_button)
    public void onSignInClicked(View view) {
        finish();
    }

    @OnClick(R.id.sign_up_button)
    public void onSignUpClicked(View view) {
        String name = mInputName.getText().toString().trim();
        String email = mInputEmail.getText().toString().trim();
        String password = mInputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter your name!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        //create user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    Log.d(TAG, "onComplete createUserWithEmail: " + task.isSuccessful());
                    mProgressBar.setVisibility(View.GONE);
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Login successfully done");
                        //startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Log.d(TAG, "Authentication failed: " + task.getException());
                    }
                });
    }

    @OnClick(R.id.sign_up_btn_reset_password)
    public void onResetPasswordClicked(View view) {
        startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
    }
}
