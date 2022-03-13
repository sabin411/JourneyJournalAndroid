package com.ismt.ismtproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.textfield.TextInputEditText;
import com.ismt.ismtproject.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {
    private Button loginButtonId;
    private TextInputEditText emailInputField,passwordInputField;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("journal_prefs", Context.MODE_PRIVATE);
        emailInputField = findViewById(R.id.emailInputField);
        passwordInputField = findViewById(R.id.passwordInputField);
         loginButtonId = findViewById(R.id.loginButtonId);


         loginButtonId.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//               When I press this button, it simply inserts the boolean user data into
                 SharedPreferences.Editor editor = preferences.edit();
                 editor.putBoolean("user_login", true).apply();

                 Intent intent = new Intent(LoginActivity.this, DashboardJourneyJournal.class);
                 intent.putExtra("email", emailInputField.getText().toString().trim());
                 intent.putExtra("password", passwordInputField.getText().toString().trim());
             }
         });

    }
}