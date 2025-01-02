package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.QuestionRepository;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.usecases.implementation.QuestionUseCase;
import com.example.quiz.domain.usecases.interfaces.IQuestionUseCase;

public class EditQuestionActivity extends AppCompatActivity {
    private EditText etQuestionText, etOption1, etOption2, etOption3, etOption4, etOption5, etCorrectAnswer, etPoints;
    private QuestionDTO questionDTO;
    private IQuestionUseCase questionUseCase;

    private String originalQuestionText, originalCorrectAnswer;
    private int originalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        initializeUIElements();
        initializeDependencies();

        if (!loadQuestionFromIntent()) {
            Toast.makeText(this, "Error: Question data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupSaveButtonListener();
    }

    private void initializeUIElements() {
        etQuestionText = findViewById(R.id.etQuestionText);
        etOption1 = findViewById(R.id.etOption1);
        etOption2 = findViewById(R.id.etOption2);
        etOption3 = findViewById(R.id.etOption3);
        etOption4 = findViewById(R.id.etOption4);
        etOption5 = findViewById(R.id.etOption5);
        etCorrectAnswer = findViewById(R.id.etCorrectAnswer);
        etPoints = findViewById(R.id.etPoints);
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionUseCase = new QuestionUseCase(new QuestionRepository(dbHelper));
    }

    private boolean loadQuestionFromIntent() {
        questionDTO = getIntent().getParcelableExtra("question");
        if (questionDTO != null) {
            populateUIWithQuestionData(questionDTO);
            return true;
        }
        return false;
    }

    private void populateUIWithQuestionData(QuestionDTO question) {
        etQuestionText.setText(question.getText());
        etOption1.setText(question.getOption1());
        etOption2.setText(question.getOption2());
        etOption3.setText(question.getOption3());
        etOption4.setText(question.getOption4());
        etOption5.setText(question.getOption5());
        etCorrectAnswer.setText(question.getCorrectAnswer());
        etPoints.setText(String.valueOf(question.getNumberOfPoints()));

        originalQuestionText = question.getText();
        originalCorrectAnswer = question.getCorrectAnswer();
        originalPoints = question.getNumberOfPoints();
    }

    private void setupSaveButtonListener() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                updateQuestionDetails();
                handleQuestionUpdate();
            }
        });
    }

    private boolean validateInputs() {
        if (etQuestionText.getText().toString().trim().isEmpty()) {
            showToast("Question text is required.");
            return false;
        }
        if (etCorrectAnswer.getText().toString().trim().isEmpty()) {
            showToast("Correct answer is required.");
            return false;
        }
        if (etPoints.getText().toString().trim().isEmpty()) {
            showToast("Points are required.");
            return false;
        }
        return true;
    }

    private void updateQuestionDetails() {
        questionDTO.setText(etQuestionText.getText().toString().trim());
        questionDTO.setOption1(etOption1.getText().toString().trim());
        questionDTO.setOption2(etOption2.getText().toString().trim());
        questionDTO.setOption3(etOption3.getText().toString().trim());
        questionDTO.setOption4(etOption4.getText().toString().trim());
        questionDTO.setOption5(etOption5.getText().toString().trim());
        questionDTO.setCorrectAnswer(etCorrectAnswer.getText().toString().trim());
        questionDTO.setNumberOfPoints(Integer.parseInt(etPoints.getText().toString().trim()));
    }

    private void handleQuestionUpdate() {
        if (isQuestionModified()) {
            boolean isUpdated = questionUseCase.updateQuestion(questionDTO);
            if (isUpdated) {
                showToast("Question updated successfully.");
                returnUpdatedQuestionToCaller();
            } else {
                showToast("Error updating question. Please try again.");
            }
        } else {
            showToast("No changes detected.");
        }
    }

    private boolean isQuestionModified() {
        return !questionDTO.getText().equals(originalQuestionText) ||
                !questionDTO.getCorrectAnswer().equals(originalCorrectAnswer) ||
                questionDTO.getNumberOfPoints() != originalPoints;
    }

    private void returnUpdatedQuestionToCaller() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedQuestion", questionDTO);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}