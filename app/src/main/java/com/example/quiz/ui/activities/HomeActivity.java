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
    private Button logoutButton, usersButton, questionsButton, quizzesButton, selectQuizButton, resultsHistoryButton;

    private UserDTO currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!initializeCurrentUser()) {
            return;
        }

        initializeUIElements();
        setupUI();
        setupListeners();
    }

    private boolean initializeCurrentUser() {
        currentUser = getIntent().getParcelableExtra("userDTO");
        if (currentUser == null) {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initializeUIElements() {
        userFullNameTextView = findViewById(R.id.userFullNameTextView);
        logoutButton = findViewById(R.id.logoutButton);
        usersButton = findViewById(R.id.usersButton);
        questionsButton = findViewById(R.id.questionsButton);
        quizzesButton = findViewById(R.id.quizzesButton);
        selectQuizButton = findViewById(R.id.selectQuizButton);
        resultsHistoryButton = findViewById(R.id.resultsHistoryButton);
    }

    private void setupUI() {
        displayUserFullName();
        toggleAdminButtonsVisibility();
    }

    private void displayUserFullName() {
        String fullName = String.format("%s %s", currentUser.getFirstName(), currentUser.getLastName());
        userFullNameTextView.setText(fullName);
    }

    private void toggleAdminButtonsVisibility() {
        boolean isAdmin = currentUser.getRole() != null && currentUser.getRole().getRoleName().equalsIgnoreCase("Admin");
        int visibility = isAdmin ? Button.VISIBLE : Button.GONE;
        usersButton.setVisibility(visibility);
        questionsButton.setVisibility(visibility);
        quizzesButton.setVisibility(visibility);
    }

    private void setupListeners() {
        logoutButton.setOnClickListener(v -> handleLogout());
        selectQuizButton.setOnClickListener(v -> navigateToActivity(SelectQuizActivity.class));
        usersButton.setOnClickListener(v -> navigateToActivity(UsersActivity.class));
        questionsButton.setOnClickListener(v -> navigateToActivity(QuestionsActivity.class));
        quizzesButton.setOnClickListener(v -> navigateToActivity(QuizzesActivity.class));
        resultsHistoryButton.setOnClickListener(v -> navigateToActivity(ResultsHistoryActivity.class));
    }

    private void handleLogout() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
        System.exit(0);
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
}