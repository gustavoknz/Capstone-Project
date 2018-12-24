package com.bora.gustavo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bora.gustavo.R;
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
        String email = mInputEmail.getText().toString();
        final String password = mInputPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);

        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the mAuth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    mProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // there was an error
                        if (password.length() < 6) {
                            mInputPassword.setError("A senha deve ter entre 6 e 14 caracteres");
                        } else {
                            Toast.makeText(LoginActivity.this, "Falha no login", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
    }
}
