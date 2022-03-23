package com.ismt.ismtproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismt.ismtproject.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {
    private Button loginButtonId;
    private TextInputLayout emailInputField,passwordInputField;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("journal_prefs", Context.MODE_PRIVATE);

         loginButtonId.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//               When I press this button, it simply inserts the boolean user data into
                 SharedPreferences.Editor editor = preferences.edit();
                 editor.putBoolean("user_login", true).apply();

                 loginUser(v);
             }
         });

    }

    private Boolean validateEmail() {
        String val = emailInputField.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            emailInputField.setError("Field cannot be empty");
            return false;
        }
        else {
            emailInputField.setError(null);
            emailInputField.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = passwordInputField.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            passwordInputField.setError("Field cannot be empty");
            return false;
        }
        else{
            passwordInputField.setError(null);
            passwordInputField.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser (View v){
//        validate login Info
        if(!validateEmail() | !validatePassword()){
            return;
        }
        else {
            isUser();
        }
    }

    private void isUser() {
        String userEnteredEmail = emailInputField.getEditText().getText().toString().trim();
        String userEnteredPassword = passwordInputField.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("email").equalTo(userEnteredEmail);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    emailInputField.setError(null);
                    emailInputField.setErrorEnabled(false);
                    passwordInputField.setError(null);
                    passwordInputField.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredEmail).child("password").getValue(String.class);

                    if(!passwordFromDB.equals(userEnteredPassword)){

                        passwordInputField.setError("wrong password");
                        passwordInputField.requestFocus();

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
                    emailInputField.setError("No such user exists");
                    emailInputField.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}