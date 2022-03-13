package com.ismt.ismtproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.ismt.ismtproject.database.User;
import com.ismt.ismtproject.database.UserRepo;

public class SignupActivity extends AppCompatActivity {
    private UserRepo userRepo;

    private Button signupButtonId;
    private TextInputEditText emailInput, passwordInput, nameInput, phoneNumberInput, addressInput;

//    validation check
    public static boolean isNull(EditText textValue, String error_message){
        textValue.setError(null);
        if(textValue.length() == 0){
            textValue.setError(error_message);
            return true;
        }
        return false;
    }
//    password and confirm password matches or not


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupButtonId = findViewById(R.id.signupButtonId);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        nameInput = findViewById(R.id.nameInput);
        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        addressInput = findViewById(R.id.addressInput);



        signupButtonId.setOnClickListener(view -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String phone = phoneNumberInput.getText().toString().trim();
            String address = addressInput.getText().toString().trim();


            isNull(emailInput, "Please Fill all inputs");
            isNull(nameInput, "Please Enter Name");
            isNull(phoneNumberInput, "Please add phone number");
            isNull(addressInput, "Please add your address");

            if(isNull(nameInput, "Please Enter Name") || isNull(emailInput, "Please Enter Email") || isNull(passwordInput, "Please Enter Password") || isNull(phoneNumberInput, "Please add phone number") || isNull(addressInput, "Please add your address")){
                Toast.makeText(SignupActivity.this, "Please fill all the inputs", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(SignupActivity.this, "You have successfully signed up", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, DashboardJourneyJournal.class);
                startActivity(intent);
                User user = new User(name, 12, phone, address,email, password);
                userRepo.createUser(user);
            }
        });


    }
}