package com.bora.gustavo.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bora.gustavo.R;
import com.bora.gustavo.helper.Utils;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends BackActivity {
    private FirebaseAuth mAuth;
    @BindView(R.id.reset_password_email)
    EditText mInputEmail;
    @BindView(R.id.reset_password_progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.reset_password_button_back)
    public void onBackClicked(View view) {
        finish();
    }

    @OnClick(R.id.reset_password_button)
    public void onResetClicked(View view) {
        String email = mInputEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Utils.showSnackbar(findViewById(android.R.id.content), R.string.snackbar_close, R.string.reset_password_no_email);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, R.string.reset_password_sent_ok, Toast.LENGTH_SHORT).show();
                    } else {
                        Utils.showSnackbar(findViewById(android.R.id.content), R.string.snackbar_close, R.string.reset_password_sent_fail);
                    }
                    mProgressBar.setVisibility(View.GONE);
                });
    }
}