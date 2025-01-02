package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.QuestionQuizRepository;
import com.example.quiz.data.repositories.QuizRepository;
import com.example.quiz.domain.models.QuizDTO;
import com.example.quiz.domain.models.UserDTO;
import com.example.quiz.domain.usecases.implementation.QuizUseCase;
import com.example.quiz.domain.usecases.interfaces.IQuizUseCase;
import com.example.quiz.ui.adapters.SelectQuizzesAdapter;

import java.util.List;

public class SelectQuizActivity extends AppCompatActivity {
    private SelectQuizzesAdapter selectQuizzesAdapter;
    private UserDTO currentUser;
    private IQuizUseCase quizUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_quiz);

        if (!initializeCurrentUser()) return;

        initializeDependencies();
        initializeUI();
        loadQuizzes();
    }

    private boolean initializeCurrentUser() {
        currentUser = getIntent().getParcelableExtra("currentUser");
        if (currentUser == null) {
            Toast.makeText(this, "User data is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        return true;
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        quizUseCase = new QuizUseCase(new QuizRepository(dbHelper), new QuestionQuizRepository(dbHelper));
    }

    private void initializeUI() {
        RecyclerView quizzesRecyclerView = findViewById(R.id.rvSelectQuiz);
        quizzesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectQuizzesAdapter = new SelectQuizzesAdapter(null, this::navigateToTakeQuizActivity);
        quizzesRecyclerView.setAdapter(selectQuizzesAdapter);
    }

    private void loadQuizzes() {
        List<QuizDTO> quizzes = quizUseCase.getAllQuizzes();

        if (quizzes == null || quizzes.isEmpty()) {
            Toast.makeText(this, "No quizzes available!", Toast.LENGTH_SHORT).show();
            return;
        }

        selectQuizzesAdapter.setQuizzes(quizzes);
    }

    private void navigateToTakeQuizActivity(QuizDTO quiz) {
        Intent intent = new Intent(this, TakeQuizActivity.class);
        intent.putExtra("quizDTO", quiz);
        intent.putExtra("userDTO", currentUser);
        startActivity(intent);
    }
}