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
import com.bora.gustavo.helper.LocationHolderSingleton;
import com.bora.gustavo.helper.Utils;
import com.bora.gustavo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BackActivity {
    private static final String TAG = "SignUpActivity";
    private DatabaseReference mDatabase;
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
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.sign_up_login_button)
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
                        addUserToMyTable();
                        finish();
                    } else {
                        Log.d(TAG, "Authentication failed: " + task.getException());
                    }
                });
    }

    private void addUserToMyTable() {
        String userId = Utils.getUserUid();
        if (userId == null) {
            Log.w(TAG, "User id is null :/");
        } else {
            Log.w(TAG, "User id is = " + userId);
            String userKey = mDatabase.push().getKey();
            Log.w(TAG, "User key is = " + userKey);
            User newUser = new User();
            newUser.setName(mInputName.getText().toString());
            newUser.setJoinedAt(new Date());
            newUser.setVotesDown(0);
            newUser.setVotesUp(0);
            newUser.setFacebookId("");
            LocationHolderSingleton locationHolder = LocationHolderSingleton.getInstance();
            newUser.setLastLatitude(locationHolder.getLocation().getLatitude());
            newUser.setLastLongitude(locationHolder.getLocation().getLongitude());
            mDatabase.child(userKey).setValue(newUser);
            /*mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user == null) {
                                Log.e(TAG, "User " + userId + " is unexpectedly null");
                                Toast.makeText(SignUpActivity.this,
                                        "Error: could not fetch user.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });*/
        }
    }

    @OnClick(R.id.sign_up_reset_password_button)
    public void onResetPasswordClicked(View view) {
        startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
    }
}
