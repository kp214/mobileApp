package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Login");
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressbar);

        showHidePassword();

        TextView textViewRegister = findViewById(R.id.textView_register);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainActivity2.this, RegisterActivity.class);
                startActivity(registerActivity);
            }
        });
        auth = FirebaseAuth.getInstance();
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginPwd.getText().toString();
                //verify information entered
                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(MainActivity2.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email address is required!");
                    editTextLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(MainActivity2.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Password is required!");
                    editTextLoginPwd.requestFocus();
                } else if (textPassword.length() < 6) {
                    Toast.makeText(MainActivity2.this, "Please enter password longer than 6 characters", Toast.LENGTH_SHORT).show();
                    editTextLoginPwd.setError("Password is required to be more than 6 characters!");
                    editTextLoginPwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail, textPassword);
                }
            }
        });
    }

    private void loginUser(String textEmail, String textPassword) {
        auth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(MainActivity2.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(MainActivity2.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent userProfileActivity = new Intent(MainActivity2.this, UserProfileActivity.class);
                    startActivity(userProfileActivity);
                } else {
                    try {
                        throw task.getException();
                    } catch(Exception e) {
                        Toast.makeText(MainActivity2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }


    private void showHidePassword() {
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.visibility);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.visibility);
                } else {
                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.visibility_lock);
                }
            }
        });
    }
}