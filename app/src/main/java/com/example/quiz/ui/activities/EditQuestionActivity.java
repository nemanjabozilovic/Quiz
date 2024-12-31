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

        etQuestionText = findViewById(R.id.etQuestionText);
        etOption1 = findViewById(R.id.etOption1);
        etOption2 = findViewById(R.id.etOption2);
        etOption3 = findViewById(R.id.etOption3);
        etOption4 = findViewById(R.id.etOption4);
        etOption5 = findViewById(R.id.etOption5);
        etCorrectAnswer = findViewById(R.id.etCorrectAnswer);
        etPoints = findViewById(R.id.etPoints);
        Button btnSave = findViewById(R.id.btnSave);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionUseCase = new QuestionUseCase(new QuestionRepository(dbHelper));

        questionDTO = getIntent().getParcelableExtra("question");
        if (questionDTO != null) {
            loadQuestionData(questionDTO);
        } else {
            Toast.makeText(this, "Error: Question data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                updateQuestionDetails();

                if (!questionDTO.getText().equals(originalQuestionText) ||
                        !questionDTO.getCorrectAnswer().equals(originalCorrectAnswer) ||
                        questionDTO.getNumberOfPoints() != originalPoints) {

                    boolean isUpdated = questionUseCase.updateQuestion(questionDTO);
                    if (isUpdated) {
                        Toast.makeText(EditQuestionActivity.this, "Question updated successfully.", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedQuestion", questionDTO);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(EditQuestionActivity.this, "Error updating question. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditQuestionActivity.this, "No changes detected.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadQuestionData(QuestionDTO question) {
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

    private boolean validateInputs() {
        if (etQuestionText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Question text is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etCorrectAnswer.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Correct answer is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPoints.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Points are required.", Toast.LENGTH_SHORT).show();
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
}