package com.example.quiz.ui.activities;

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

public class AddQuestionActivity extends AppCompatActivity {

    private EditText etQuestionText, etOption1, etOption2, etOption3, etOption4, etOption5, etCorrectAnswer, etNumberOfPoints;

    private QuestionUseCase questionUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        etQuestionText = findViewById(R.id.etQuestionText);
        etOption1 = findViewById(R.id.etOption1);
        etOption2 = findViewById(R.id.etOption2);
        etOption3 = findViewById(R.id.etOption3);
        etOption4 = findViewById(R.id.etOption4);
        etOption5 = findViewById(R.id.etOption5);
        etCorrectAnswer = findViewById(R.id.etCorrectAnswer);
        etNumberOfPoints = findViewById(R.id.etNumberOfPoints);
        Button btnAddQuestion = findViewById(R.id.btnAddQuestion);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionUseCase = new QuestionUseCase(new QuestionRepository(dbHelper));

        btnAddQuestion.setOnClickListener(v -> addQuestion());
    }

    private void addQuestion() {
        String questionText = etQuestionText.getText().toString();
        String option1 = etOption1.getText().toString();
        String option2 = etOption2.getText().toString();
        String option3 = etOption3.getText().toString();
        String option4 = etOption4.getText().toString();
        String option5 = etOption5.getText().toString();
        String correctAnswer = etCorrectAnswer.getText().toString();
        String pointsStr = etNumberOfPoints.getText().toString();

        if (questionText.isEmpty() ||
                option1.isEmpty() ||
                option2.isEmpty() ||
                option3.isEmpty() ||
                option4.isEmpty() ||
                option5.isEmpty() ||
                correctAnswer.isEmpty() ||
                pointsStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int points = Integer.parseInt(pointsStr);

        QuestionDTO questionDTO = new QuestionDTO(0, questionText, option1, option2, option3, option4, option5, correctAnswer, points);

        boolean success = questionUseCase.insertQuestion(questionDTO);

        if (success) {
            Toast.makeText(this, "Question added successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to add question", Toast.LENGTH_SHORT).show();
        }
    }
}