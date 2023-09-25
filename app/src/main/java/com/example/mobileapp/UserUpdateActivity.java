package com.example.mobileapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;


public class UserUpdateActivity extends AppCompatActivity {
    private EditText editTextUpdateEmail, editTextUpdatePwd, editTextUpdateFirstName, editTextUpdateLastName;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        getSupportActionBar().setTitle("Update");
        updateInfo();
    }


    private void updateInfo() {
        Button buttonUpdateInfo = findViewById(R.id.button_update);
        buttonUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextUpdateEmail.getText().toString();
                String textPassword = editTextUpdatePwd.getText().toString();
                String textFname = editTextUpdateFirstName.getText().toString();
                String textLname = editTextUpdateLastName.getText().toString();
                //check to see if data is valid before updating the user
                if (textPassword.length() < 6 && textPassword.length() > 0) {
                    Toast.makeText(UserUpdateActivity.this, "Please enter password longer than 6 characters", Toast.LENGTH_SHORT).show();
                    editTextUpdatePwd.setError("Password is required to be more than 6 characters!");
                    editTextUpdatePwd.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE); //----------------------------ADD progressbar -----------------------**********-------------------------
                    updateUserEmail(textEmail);
                    updateUserPassword(textPassword);
                }
                Intent userProfileActivity = new Intent(UserUpdateActivity.this, UserProfileActivity.class);
                userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(userProfileActivity);
                finish();
            }


            private void updateUserEmail(String textEmail) {
                if (!textEmail.isEmpty()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateEmail(textEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserUpdateActivity.this, "Email Update Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            private void updateUserPassword(String textPassword) {
                if (!textPassword.isEmpty()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(textPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //check to see if the user was created successfully
                                    FirebaseUser firebaseUser = null;
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserUpdateActivity.this, "Password Update Successful", Toast.LENGTH_SHORT).show();
                                        firebaseUser = auth.getCurrentUser();
                                    }
                                }
                            });
                }
            }


                   /* @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check to see if the user was created successfully
                        FirebaseUser firebaseUser = null;
                        if(task.isSuccessful()){
                            firebaseUser = auth.getCurrentUser();
                        }
                        if(firebaseUser != null){
                            //update display name of the user
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(textFname + " " + textLname).build();
                            firebaseUser.updateProfile(profileUpdates);
                            Toast.makeText(UserUpdateActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                            updateUserInfo(textEmail, textFname, textPassword);
                            //open the userProfileActivity after the user is created
                            Intent userProfileActivity = new Intent(UserUpdateActivity.this, UserProfileActivity.class);
                            //STOP the user from going back to the update screen
                            userProfileActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(userProfileActivity);
                            finish();
                        } else {
                            //handle the exceptions
                            try {
                                throw task.getException();


                            } catch(Exception e) {
                                Toast.makeText(UserUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }*/

            private void updateUserInfo(String textEmail, String textFname, String textPassword) {
                User user = new User(textFname, textEmail, textPassword);
                database.child("users").child("" + user.getID());
            }

            private void findViews() {
                editTextUpdateEmail = findViewById(R.id.editText_update_email);
                editTextUpdatePwd = findViewById(R.id.editText_update_pwd);
                editTextUpdateFirstName = findViewById(R.id.editText_update_fname);
                editTextUpdateLastName = findViewById(R.id.editText_update_lname);
                progressBar = findViewById(R.id.progressbar);
            }

        });
    }
}
