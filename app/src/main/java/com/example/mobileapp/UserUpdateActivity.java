package com.example.mobileapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.foundation.gestures.ForEachGestureKt;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class UserUpdateActivity extends AppCompatActivity {
    private EditText editTextUpdateEmail, editTextUpdatePwd, editTextUpdateUsername;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        getSupportActionBar().setTitle("Update");
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        database = firebaseDatabase.getReference();
        findViews();
        showCurrentUser(firebaseUser);
        updateUserInfo();
    }

    private void showCurrentUser(FirebaseUser firebaseUser) {
        database.child("users").child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue().toString();
                editTextUpdateUsername.setText(username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserUpdateActivity.this, "error displaying info", Toast.LENGTH_SHORT).show();
            }
        });
        database.child("users").child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getValue().toString();
                editTextUpdateEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserUpdateActivity.this, "error displaying info", Toast.LENGTH_SHORT).show();
            }
        });
        database.child("users").child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String password = dataSnapshot.getValue().toString();
                editTextUpdatePwd.setText(password);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserUpdateActivity.this, "error displaying info", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUserInfo() {
        Button buttonUpdateInfo = findViewById(R.id.button_update);
        buttonUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextUpdateEmail.getText().toString();
                String textPassword = editTextUpdatePwd.getText().toString();
                String textUsername = editTextUpdateUsername.getText().toString();
                //check to see if data is valid before updating the user
                if (textPassword.length() < 6 && textPassword.length() > 0) {
                    Toast.makeText(UserUpdateActivity.this, "Please enter password longer than 6 characters", Toast.LENGTH_SHORT).show();
                    editTextUpdatePwd.setError("Password is required to be more than 6 characters!");
                    editTextUpdatePwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //updateUserEmail(textEmail, user);
                    //updateUserPassword(textPassword, user);
                    updateUsername(textUsername, user);
                    updateEmailAndPassword(textEmail, textPassword, user);
                    updateDatabase(textUsername, textEmail, textPassword);
                }
                Intent userProfileActivity = new Intent(UserUpdateActivity.this, UserProfileActivity.class);
                userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(userProfileActivity);
                finish();
            }


            private void updateUserEmail(String textEmail, FirebaseUser user) {
                    user.updateEmail(textEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserUpdateActivity.this, "Info Update Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }

            private void updateEmailAndPassword(String textEmail, String textPassword, FirebaseUser user) {
                auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(UserUpdateActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check to see if the user was created successfully
                        FirebaseUser tempUser = null;
                        if (task.isSuccessful()) {
                            tempUser = auth.getCurrentUser();
                            auth.updateCurrentUser(tempUser); //keeps creating new users, but it works
                            tempUser.delete();//------------------------------------------------------------------NOT DELETING-------------------************************--------------------
                        }
                    }
                });
            }

            private void updateUsername(String textUsername, FirebaseUser user) {
                UserProfileChangeRequest profileUpdateUsername = new UserProfileChangeRequest.Builder().setDisplayName(textUsername).build();
                    user.updateProfile(profileUpdateUsername).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserUpdateActivity.this, "Info Update Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }

            private void updateUserPassword(String textPassword, FirebaseUser user) {
                    user.updatePassword(textPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserUpdateActivity.this, "Info Update Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
        });
    }

    private void updateDatabase(String username, String email, String password) {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child("username").setValue(username);
        database.child("users").child("email").setValue(email);
        database.child("users").child("password").setValue(password);
    }

    private void findViews() {
        editTextUpdateEmail = findViewById(R.id.editText_update_email);
        editTextUpdatePwd = findViewById(R.id.editText_update_pwd);
        editTextUpdateUsername = findViewById(R.id.editText_update_username);
        progressBar = findViewById(R.id.progressbar);
    }
}
