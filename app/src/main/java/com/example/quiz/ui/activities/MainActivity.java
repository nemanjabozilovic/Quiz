package com.example.quiz.ui.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            UserDTO userDTO = userUseCase.login(email, password);

            if (userDTO != null) {
                Toast.makeText(MainActivity.this, "Login successful: " + userDTO.getFirstName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "An error occurred during login", Toast.LENGTH_SHORT).show();
        }
    }
}