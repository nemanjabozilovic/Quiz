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
import com.example.quiz.domain.mappers.QuestionQuizMapper;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuestionQuizDTO;
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

        etQuizName = findViewById(R.id.etQuizName);
        etDate = findViewById(R.id.etDate);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnEditQuestions = findViewById(R.id.btnEditQuestions);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        quizUseCase = new QuizUseCase(new QuizRepository(dbHelper), new QuestionQuizRepository(dbHelper));
        questionQuizUseCase = new QuestionQuizUseCase(new QuestionQuizRepository(dbHelper), new QuestionRepository(dbHelper));

        quizDTO = getIntent().getParcelableExtra("quiz");
        if (quizDTO != null) {
            loadQuizData(quizDTO);
            selectedQuestions = questionQuizUseCase.getQuestionsForQuiz(quizDTO.getId());
        } else {
            Toast.makeText(this, "Error: Quiz data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                updateQuizDetails();

                boolean isUpdated = quizUseCase.updateQuiz(quizDTO);
                if (isUpdated) {
                    boolean questionsUpdated = questionQuizUseCase.updateQuestionsForQuiz(
                            quizDTO.getId(),
                            QuestionConversionUtils.convertQuestionDTOsToQuestionQuiz(selectedQuestions, quizDTO.getId())
                    );

                    if (questionsUpdated) {
                        Toast.makeText(this, "Quiz updated successfully.", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedQuiz", quizDTO);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "Error updating quiz questions.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error updating quiz details. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEditQuestions.setOnClickListener(v -> openQuestionSelectionActivity());
    }

    private void loadQuizData(QuizDTO quiz) {
        etQuizName.setText(quiz.getQuizName());
        etDate.setText(quiz.getDate());

        originalQuizName = quiz.getQuizName();
        originalDate = quiz.getDate();
    }

    private boolean validateInputs() {
        if (etQuizName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Quiz name is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Date is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateQuizDetails() {
        quizDTO.setQuizName(etQuizName.getText().toString().trim());
        quizDTO.setDate(etDate.getText().toString().trim());
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
            Toast.makeText(this, selectedQuestions.size() + " questions updated for the quiz.", Toast.LENGTH_SHORT).show();
        }
    }

    private List<QuestionQuizDTO> convertToQuestionQuizDTO(List<QuestionDTO> questions) {
        List<QuestionQuizDTO> questionQuizDTOs = new ArrayList<>();
        for (QuestionDTO question : questions) {
            QuestionQuizDTO dto = QuestionQuizDTO.fromQuestionDTO(question);
            dto.setQuizId(quizDTO.getId());
            questionQuizDTOs.add(dto);
        }
        return questionQuizDTOs;
    }
}