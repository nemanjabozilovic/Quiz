package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.domain.models.UserDTO;

public class HomeActivity extends AppCompatActivity {

    private TextView userFullNameTextView;
    private Button logoutButton;
    private Button usersButton;
    private Button questionsButton;
    private Button quizzesButton;
    private Button startQuizButton;

    private UserDTO currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        currentUser = getIntent().getParcelableExtra("userDTO");

        if (currentUser == null) {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
            return;
        }

        initializeUIElements();
        setupUI();
        setupListeners();
    }

    private void initializeUIElements() {
        userFullNameTextView = findViewById(R.id.userFullNameTextView);
        logoutButton = findViewById(R.id.logoutButton);
        usersButton = findViewById(R.id.usersButton);
        questionsButton = findViewById(R.id.questionsButton);
        quizzesButton = findViewById(R.id.quizzesButton);
        startQuizButton = findViewById(R.id.startQuizButton);
    }

    private void setupUI() {
        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        userFullNameTextView.setText(fullName);

        if (currentUser.getRole() != null && currentUser.getRole().getRoleName().equalsIgnoreCase("Admin")) {
            usersButton.setVisibility(Button.VISIBLE);
            questionsButton.setVisibility(Button.VISIBLE);
            quizzesButton.setVisibility(Button.VISIBLE);
        } else {
            usersButton.setVisibility(Button.GONE);
            questionsButton.setVisibility(Button.GONE);
            quizzesButton.setVisibility(Button.GONE);
        }
    }

    private void setupListeners() {
        logoutButton.setOnClickListener(v -> handleLogout());
        startQuizButton.setOnClickListener(v -> startQuiz());
        usersButton.setOnClickListener(v -> navigateToUsers());
        questionsButton.setOnClickListener(v -> navigateToQuestions());
        quizzesButton.setOnClickListener(v -> navigateToQuizzes());
    }

    private void handleLogout() {
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void startQuiz() {
        Toast.makeText(this, "Starting quiz...", Toast.LENGTH_SHORT).show();
    }

    private void navigateToUsers() {
        Intent intent = new Intent(this, UsersActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    private void navigateToQuestions() {
        Intent intent = new Intent(this, QuestionsActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    private void navigateToQuizzes() {
    }
}