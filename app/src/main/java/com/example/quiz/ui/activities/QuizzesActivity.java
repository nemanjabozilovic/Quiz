package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.QuestionQuizRepository;
import com.example.quiz.data.repositories.QuestionRepository;
import com.example.quiz.data.repositories.QuizRepository;
import com.example.quiz.domain.models.QuizDTO;
import com.example.quiz.domain.usecases.implementation.QuestionQuizUseCase;
import com.example.quiz.domain.usecases.implementation.QuizUseCase;
import com.example.quiz.ui.adapters.QuizzesAdapter;

import java.util.List;

public class QuizzesActivity extends AppCompatActivity {
    private QuizzesAdapter quizzesAdapter;
    private QuizUseCase quizUseCase;
    private QuestionQuizUseCase questionQuizUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes);

        RecyclerView recyclerView = findViewById(R.id.rvQuizzes);
        Button btnAddQuiz = findViewById(R.id.btnAddQuiz);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        quizUseCase = new QuizUseCase(new QuizRepository(dbHelper), new QuestionQuizRepository(dbHelper));
        questionQuizUseCase = new QuestionQuizUseCase(new QuestionQuizRepository(dbHelper), new QuestionRepository(dbHelper));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizzesAdapter = new QuizzesAdapter(quizUseCase.getAllQuizzes(), new QuizzesAdapter.OnQuizClickListener() {
            @Override
            public void onQuizClick(QuizDTO quiz) {
                String quizDetails = "ID: " + quiz.getId() + "\n" +
                        "Quiz Name: " + quiz.getQuizName() + "\n" +
                        "Creation Date: " + quiz.getDate() + "\n" +
                        "Number of Questions: " + questionQuizUseCase.getNumberOfQuestionsForQuiz(quiz.getId());

                new AlertDialog.Builder(QuizzesActivity.this)
                        .setTitle("Quiz Details")
                        .setMessage(quizDetails)
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onEditClick(QuizDTO quiz) {
                Intent intent = new Intent(QuizzesActivity.this, EditQuizActivity.class);
                intent.putExtra("quiz", quiz);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onDeleteClick(int quizId) {
                if (quizUseCase.deleteQuiz(quizId)) {
                    Toast.makeText(QuizzesActivity.this, "Quiz deleted successfully.", Toast.LENGTH_SHORT).show();
                    loadQuizzes();
                } else {
                    Toast.makeText(QuizzesActivity.this, "Error deleting quiz.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(quizzesAdapter);

        loadQuizzes();

        btnAddQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(QuizzesActivity.this, AddQuizActivity.class);
            startActivityForResult(intent, 2);
        });
    }

    private void loadQuizzes() {
        List<QuizDTO> quizzes = quizUseCase.getAllQuizzes();
        quizzesAdapter.setQuizzes(quizzes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            loadQuizzes();
        }
    }
}