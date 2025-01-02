package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.QuestionQuizRepository;
import com.example.quiz.data.repositories.QuestionRepository;
import com.example.quiz.data.repositories.QuizRepository;
import com.example.quiz.domain.mappers.QuestionConversionUtils;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuizDTO;
import com.example.quiz.domain.usecases.implementation.QuestionQuizUseCase;
import com.example.quiz.domain.usecases.implementation.QuizUseCase;

import java.util.ArrayList;
import java.util.List;

public class EditQuizActivity extends AppCompatActivity {
    private EditText etQuizName, etDate;
    private QuizDTO quizDTO;
    private QuizUseCase quizUseCase;
    private QuestionQuizUseCase questionQuizUseCase;
    private List<QuestionDTO> selectedQuestions = new ArrayList<>();
    private String originalQuizName, originalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        initializeUIElements();
        initializeDependencies();

        if (!loadQuizFromIntent()) {
            showToast("Error: Quiz data not found.");
            finish();
            return;
        }

        setupSaveButtonListener();
        setupEditQuestionsButtonListener();
    }

    private void initializeUIElements() {
        etQuizName = findViewById(R.id.etQuizName);
        etDate = findViewById(R.id.etDate);
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        quizUseCase = new QuizUseCase(new QuizRepository(dbHelper), new QuestionQuizRepository(dbHelper));
        questionQuizUseCase = new QuestionQuizUseCase(new QuestionQuizRepository(dbHelper), new QuestionRepository(dbHelper));
    }

    private boolean loadQuizFromIntent() {
        quizDTO = getIntent().getParcelableExtra("quiz");
        if (quizDTO != null) {
            populateQuizData(quizDTO);
            selectedQuestions = questionQuizUseCase.getQuestionsForQuiz(quizDTO.getId());
            return true;
        }
        return false;
    }

    private void populateQuizData(QuizDTO quiz) {
        etQuizName.setText(quiz.getQuizName());
        etDate.setText(quiz.getDate());

        originalQuizName = quiz.getQuizName();
        originalDate = quiz.getDate();
    }

    private void setupSaveButtonListener() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                updateQuizDetails();
                handleQuizUpdate();
            }
        });
    }

    private void setupEditQuestionsButtonListener() {
        Button btnEditQuestions = findViewById(R.id.btnEditQuestions);
        btnEditQuestions.setOnClickListener(v -> openQuestionSelectionActivity());
    }

    private boolean validateInputs() {
        if (etQuizName.getText().toString().trim().isEmpty()) {
            showToast("Quiz name is required.");
            return false;
        }
        if (etDate.getText().toString().trim().isEmpty()) {
            showToast("Date is required.");
            return false;
        }
        return true;
    }

    private void updateQuizDetails() {
        String newQuizName = etQuizName.getText().toString().trim();
        String newDate = etDate.getText().toString().trim();

        if (!newQuizName.equals(originalQuizName)) {
            quizDTO.setQuizName(newQuizName);
        }

        if (!newDate.equals(originalDate)) {
            quizDTO.setDate(newDate);
        }
    }

    private void handleQuizUpdate() {
        boolean isQuizUpdated = quizUseCase.updateQuiz(quizDTO);

        if (isQuizUpdated) {
            boolean areQuestionsUpdated = updateQuizQuestions();
            if (areQuestionsUpdated) {
                showToast("Quiz updated successfully.");
                returnUpdatedQuiz();
            } else {
                showToast("Error updating quiz questions.");
            }
        } else {
            showToast("Error updating quiz details. Please try again.");
        }
    }

    private boolean updateQuizQuestions() {
        return questionQuizUseCase.updateQuestionsForQuiz(
                quizDTO.getId(),
                QuestionConversionUtils.convertQuestionDTOsToQuestionQuiz(selectedQuestions, quizDTO.getId())
        );
    }

    private void returnUpdatedQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedQuiz", quizDTO);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void openQuestionSelectionActivity() {
        Intent intent = new Intent(this, SelectQuestionsActivity.class);
        intent.putParcelableArrayListExtra("selectedQuestions", new ArrayList<>(selectedQuestions));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedQuestions = data.getParcelableArrayListExtra("selectedQuestions");
            showToast(selectedQuestions.size() + " questions updated for the quiz.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}