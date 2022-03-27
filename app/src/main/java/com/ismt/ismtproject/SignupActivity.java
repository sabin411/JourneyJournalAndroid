package com.ismt.ismtproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismt.ismtproject.database.User;
import com.ismt.ismtproject.database.UserRepo;
import com.ismt.ismtproject.ui.UserHelperClass;

import java.util.HashMap;
import java.util.Objects;

import cool.graph.cuid.Cuid;
//import cool.graph.cuid.Cuid;

public class SignupActivity extends AppCompatActivity {
    private UserRepo userRepo;
    private Button signupButtonId;
    private TextInputEditText emailInput, passwordInput, nameInput, phoneNumberInput, addressInput;
    private TextView loginLink;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    FirebaseAuth mAuth;

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
        loginLink = findViewById(R.id.loginLink);
        mAuth = FirebaseAuth.getInstance();



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

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser rUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert rUser != null;
                            String userId = rUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("userId", userId);
                            hashMap.put("name", name);
                            hashMap.put("phone", phone);
                            hashMap.put("address", address);
                            hashMap.put("imageUrl", "default");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    }
                                    else{
                                        Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(SignupActivity.this, "Error occurred" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//                Toast.makeText(SignupActivity.this, "You have successfully signed up", Toast.LENGTH_SHORT).show();
//
//
//
//                rootNode = FirebaseDatabase.getInstance();
//                reference = rootNode.getReference();
//
//                UserHelperClass helperClass = new UserHelperClass(name, email, phone, address, password);
//                String cuid = Cuid.createCuid();
//
//
//                reference.child("users").child(cuid).setValue(helperClass).addOnSuccessListener(
//                        new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                System.out.println("success");
//                            }
//                        }
//                ).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        System.out.println("failed" + e);
//                    }
//                });
//                Intent intent = new Intent(SignupActivity.this, DashboardJourneyJournal.class);
//                startActivity(intent);
//                User user = new User(name, 12, phone, address,email, password);
//                userRepo.createUser(user);
            }
        });
        loginLink.setOnClickListener(view -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });
    }
}