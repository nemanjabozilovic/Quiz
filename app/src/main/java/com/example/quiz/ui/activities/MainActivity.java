package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.RoleRepository;
import com.example.quiz.data.repositories.UserRepository;
import com.example.quiz.data.repositories.UserRolesRepository;
import com.example.quiz.domain.models.UserDTO;
import com.example.quiz.domain.repositories.IRoleRepository;
import com.example.quiz.domain.repositories.IUserRepository;
import com.example.quiz.domain.repositories.IUserRolesRepository;
import com.example.quiz.domain.usecases.implementation.UserUseCase;
import com.example.quiz.domain.usecases.interfaces.IUserUseCase;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private IUserUseCase userUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();
        setupListeners();
        initializeDependencies();
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        IUserRepository userRepository = new UserRepository(dbHelper);
        IUserRolesRepository userRolesRepository = new UserRolesRepository(dbHelper);
        IRoleRepository roleRepository = new RoleRepository(dbHelper);

        userUseCase = new UserUseCase(userRepository, userRolesRepository, roleRepository);
    }

    private void initializeUIElements() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> handleLogin());
    }

    public void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showToast("Please enter both email and password");
            return;
        }

        try {
            UserDTO userDTO = userUseCase.login(email, password);

            if (userDTO != null) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("userDTO", userDTO);
                startActivity(intent);
                finish();
            } else {
                showToast("Invalid email or password");
            }
        } catch (Exception e) {
            showToast("An error occurred during login");
        }
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}