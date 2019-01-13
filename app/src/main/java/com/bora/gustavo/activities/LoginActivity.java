package com.bora.gustavo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bora.gustavo.R;
import com.bora.gustavo.helper.Utils;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BackActivity {
    private FirebaseAuth mAuth;
    @BindView(R.id.login_email)
    EditText mInputEmail;
    @BindView(R.id.login_password)
    EditText mInputPassword;
    @BindView(R.id.login_progress_bar)
    ProgressBar mProgressBar;

    @OnClick(R.id.login_button_signup)
    public void signUpCLicked(View view) {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    @OnClick(R.id.login_button_reset_password)
    public void resetPasswordCLicked(View view) {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    @OnClick(R.id.login_button)
    public void loginCLicked(View view) {
        String email = mInputEmail.getText().toString().trim();
        String password = mInputPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Utils.showSnackbar(findViewById(android.R.id.content), R.string.snackbar_close, R.string.login_fail_email);
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 14) {
            Utils.showSnackbar(findViewById(android.R.id.content), R.string.snackbar_close, R.string.login_fail_password);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    mProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Utils.showSnackbar(findViewById(android.R.id.content), R.string.snackbar_close, R.string.login_fail);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }
}
