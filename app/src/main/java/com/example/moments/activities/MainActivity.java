package com.example.moments.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moments.R;
import com.example.moments.databinding.ActivityMainBinding;
import com.example.moments.util.AppState;

public class MainActivity extends AppCompatActivity {

    private EditText enteredUsername, enteredPassword;
    private final String[] validUsernames = { "admin", "pera" };
    private final String[] validPasswords = { "a", "p" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.moments.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // clearing old user preferences: user cart is emptied, all users will have default data
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // make password not readable
//        @SuppressLint("CutPasteId") EditText password = (EditText) findViewById(R.id.lozinka);
//        password.setTransformationMethod(new PasswordTransformationMethod());

        enteredUsername = findViewById(R.id.korime);
        enteredPassword = findViewById(R.id.lozinka);
        enteredPassword.setTransformationMethod(new PasswordTransformationMethod());
        Button loginButton = findViewById(R.id.loginDugme);

        loginButton.setOnClickListener(v -> {
            if(validCredentials()) {
                saveLoginState();
                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                navigateHome();
            } else {
                Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validCredentials() {
        String username = enteredUsername.getText().toString();
        String password = enteredPassword.getText().toString();

        if(!username.isEmpty() && !password.isEmpty()) {
            for(int i = 0; i < validUsernames.length; i++) {
                if(username.equals(validUsernames[i]) && password.equals(validPasswords[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    // saving user data in the local storage (preferences)
    private void saveLoginState() {
        AppState as = AppState.getInstance(this);
        as.saveLoginData(enteredUsername.getText().toString(), enteredPassword.getText().toString());
    }

    // navigating to the app's home page
    private void navigateHome() {
        startActivity(new Intent(MainActivity.this, HomePageActivity.class));
        finish();
    }


}