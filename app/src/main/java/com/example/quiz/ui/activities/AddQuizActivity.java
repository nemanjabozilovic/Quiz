package com.example.quiz.ui.activities;

import android.app.DatePickerDialog;
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
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuestionQuizDTO;
import com.example.quiz.domain.models.QuizDTO;
import com.example.quiz.domain.usecases.implementation.QuestionQuizUseCase;
import com.example.quiz.domain.usecases.implementation.QuizUseCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddQuizActivity extends AppCompatActivity {

    private EditText etQuizName, etDate;
    private QuizUseCase quizUseCase;
    private QuestionQuizUseCase questionQuizUseCase;
    private List<QuestionDTO> selectedQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);

        etQuizName = findViewById(R.id.etQuizName);
        etDate = findViewById(R.id.etDate);
        Button btnAddQuiz = findViewById(R.id.btnAddQuiz);
        Button btnAddQuestions = findViewById(R.id.btnAddQuestions);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        quizUseCase = new QuizUseCase(new QuizRepository(dbHelper), new QuestionQuizRepository(dbHelper));
        questionQuizUseCase = new QuestionQuizUseCase(new QuestionQuizRepository(dbHelper), new QuestionRepository(dbHelper));

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                etDate.setText(selectedDate);
            }, year, month, day);

            datePickerDialog.show();
        });

        btnAddQuestions.setOnClickListener(v -> openQuestionSelectionActivity());

        btnAddQuiz.setOnClickListener(v -> addQuiz());
    }

    private void openQuestionSelectionActivity() {
        Intent intent = new Intent(this, SelectQuestionsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedQuestions = data.getParcelableArrayListExtra("selectedQuestions");
            Toast.makeText(this, selectedQuestions.size() + " questions added to the quiz.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addQuiz() {
        String quizName = etQuizName.getText().toString();
        String date = etDate.getText().toString();

        if (quizName.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedQuestions.isEmpty()) {
            Toast.makeText(this, "Please select at least one question for the quiz.", Toast.LENGTH_SHORT).show();
            return;
        }

        QuizDTO quizDTO = new QuizDTO(0, quizName, date);
        boolean success = quizUseCase.insertQuiz(quizDTO);

        if (success) {
            int quizId = quizUseCase.getLastQuizId();

            for (QuestionDTO selectedQuestion : selectedQuestions) {
                QuestionQuizDTO questionQuizDTO = QuestionQuizDTO.fromQuestionDTO(selectedQuestion);
                questionQuizDTO.setQuizId(quizId);
                questionQuizUseCase.insertQuestionQuiz(questionQuizDTO);
            }

            Toast.makeText(this, "Quiz created successfully with " + selectedQuestions.size() + " questions.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, QuizzesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to create quiz", Toast.LENGTH_SHORT).show();
        }
    }
}