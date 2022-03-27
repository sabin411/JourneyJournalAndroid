package com.ismt.ismtproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismt.ismtproject.databinding.ActivityMainBinding;

import java.util.logging.LoggingMXBean;

public class LoginActivity extends AppCompatActivity {
    private Button loginButtonId;
    private TextInputEditText emailInput,passwordInput;
    private SharedPreferences preferences;
    private TextView newUserSignUP;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButtonId = findViewById(R.id.loginButtonId);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        newUserSignUP = findViewById(R.id.newUserSignUP);
        mAuth = FirebaseAuth.getInstance();
        loginButtonId.setOnClickListener(view -> {
            if(!validateEmail() | !validatePassword()){
                Toast.makeText(this, "Is empty", Toast.LENGTH_SHORT);
                return;
            }
            else {
                String userEnteredEmail = emailInput.getText().toString().trim();
                String userEnteredPassword = passwordInput.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(userEnteredEmail, userEnteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, DashboardJourneyJournal.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        newUserSignUP.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

    }
//    on start
//
    private Boolean validateEmail() {
        String val = emailInput.getText().toString().trim();

        if (val.isEmpty()){
            emailInput.setError("Field cannot be empty");
            return false;
        }
        else {
            emailInput.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = passwordInput.getText().toString().trim();

        if (val.isEmpty()){
            passwordInput.setError("Field cannot be empty");
            return false;
        }
        else{
            passwordInput.setError(null);
            return true;
        }
    }
//
//    public void loginUser (View v){
////        validate login Info
//        if(!validateEmail() | !validatePassword()){
//            return;
//        }
//        else {
//            isUser();
//        }
//    }
////
    private void isUser() {
        Toast.makeText(this, "isused", Toast.LENGTH_LONG);
        String userEnteredEmail = emailInput.getText().toString().trim();
        String userEnteredPassword = passwordInput.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(userEnteredEmail);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    emailInput.setError(null);
                    passwordInput.setError(null);

                    String passwordFromDB = snapshot.child(userEnteredEmail).child("password").getValue(String.class);

                    if(!passwordFromDB.equals(userEnteredPassword)){

                        passwordInput.setError("wrong password");
                        passwordInput.requestFocus();

                    }else{
                        String nameFromDB = snapshot.child(userEnteredEmail).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredEmail).child("email").getValue(String.class);
                        String phoneFromDB = snapshot.child(userEnteredEmail).child("phone").getValue(String.class);
                        String addressFromDB = snapshot.child(userEnteredEmail).child("address").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), DashboardJourneyJournal.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phone", phoneFromDB);
                        intent.putExtra("address", addressFromDB);
                        intent.putExtra("password", passwordFromDB);
                    }
                }
                else {
                    emailInput.setError("No such user exists");
                    emailInput.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}