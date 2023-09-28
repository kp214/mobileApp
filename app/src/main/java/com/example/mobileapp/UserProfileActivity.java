package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileapp.UserUpdateActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewUsername, textViewLastName, textViewEmail, textViewRegisterDate;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Home");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        findViews();
        signOut();
        update();
        //show profile details if the user is not null
        if(firebaseUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        } else {
            Toast.makeText(UserProfileActivity.this, "user not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        //to obtain metadata about the user object
        FirebaseUserMetadata metadata = firebaseUser.getMetadata();
        //grab the register date of the user
        long registerTimeStamp = metadata.getCreationTimestamp();
        String datePattern = "E, dd MMM yyy hh:mm a z"; //day, dd MMM yyy hh:mm AM/PM timezone
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        sdf.setTimeZone(TimeZone.getDefault());
        String register = sdf.format(new Date(registerTimeStamp));
        String registerDate = getResources().getString(R.string.user_since, register);
        String name = firebaseUser.getDisplayName();
        String email = firebaseUser.getEmail();
        textViewEmail.setText(email);
        textViewUsername.setText(name);
        textViewWelcome.setText("Welcome " + name);
        progressBar.setVisibility(View.GONE);
    }

    private void signOut() {
        Button buttonSignOut = findViewById(R.id.button_sign_out);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(UserProfileActivity.this, "signed out", Toast.LENGTH_SHORT).show();
                //clear the stack so user can't use the back arrow to get in
                Intent mainActivity = new Intent(UserProfileActivity.this, MainActivity2.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                finish();
            }
        });
    }

    private void update() {
        Button buttonUpdate = findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserProfileActivity.this, "update page", Toast.LENGTH_SHORT).show();
                //clear the stack so user can't use the back arrow to get in
                Intent userUpdateActivity = new Intent(UserProfileActivity.this, UserUpdateActivity.class);
                startActivity(userUpdateActivity);
                finish();
            }
        });
    }

    private void findViews() {
        textViewWelcome = findViewById(R.id.textView_welcome);
        textViewUsername = findViewById(R.id.textView_show_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewRegisterDate = findViewById(R.id.textView_register);
        progressBar = findViewById(R.id.progressbar);
    }
}