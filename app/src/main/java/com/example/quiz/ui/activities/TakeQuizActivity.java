package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.QuestionQuizRepository;
import com.example.quiz.data.repositories.QuestionRepository;
import com.example.quiz.data.repositories.UserQuizRepository;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.models.QuizDTO;
import com.example.quiz.domain.models.UserDTO;
import com.example.quiz.domain.models.UserQuizDTO;
import com.example.quiz.domain.usecases.implementation.QuestionQuizUseCase;
import com.example.quiz.domain.usecases.implementation.UserQuizUseCase;
import com.example.quiz.domain.usecases.interfaces.IQuestionQuizUseCase;
import com.example.quiz.domain.usecases.interfaces.IUserQuizUseCase;

import java.util.List;

public class TakeQuizActivity extends AppCompatActivity {
    private TextView tvQuestionText, tvFeedback;
    private RadioGroup rgOptions;
    private Button btnSubmit, btnNextQuestion, btnHome;

    private QuizDTO selectedQuiz;
    private UserDTO currentUser;
    private List<QuestionDTO> questions;
    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private IQuestionQuizUseCase questionQuizUseCase;
    private IUserQuizUseCase userQuizUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        selectedQuiz = getIntent().getParcelableExtra("quizDTO");
        currentUser = getIntent().getParcelableExtra("userDTO");

        if (selectedQuiz == null || currentUser == null) {
            Toast.makeText(this, "Invalid data provided!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionQuizUseCase = new QuestionQuizUseCase(new QuestionQuizRepository(dbHelper), new QuestionRepository(dbHelper));
        userQuizUseCase = new UserQuizUseCase(new UserQuizRepository(dbHelper));

        initializeUI();
        loadQuestions();
        displayQuestion(currentQuestionIndex);
    }

    private void initializeUI() {
        tvQuestionText = findViewById(R.id.tvQuestionText);
        rgOptions = findViewById(R.id.rgOptions);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);
        btnHome = findViewById(R.id.btnHome);
        tvFeedback = findViewById(R.id.tvFeedback);

        btnSubmit.setOnClickListener(v -> submitAnswer());
        btnNextQuestion.setOnClickListener(v -> navigateToNextQuestion());
        btnHome.setOnClickListener(v -> navigateToHome());

        btnSubmit.setEnabled(false);

        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            btnSubmit.setEnabled(checkedId != -1);
        });
    }

    private void loadQuestions() {
        questions = questionQuizUseCase.getQuestionsForQuiz(selectedQuiz.getId());
        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "No questions available for this quiz!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayQuestion(int index) {
        if (questions != null && index >= 0 && index < questions.size()) {
            QuestionDTO question = questions.get(index);
            tvQuestionText.setText(question.getText());

            rgOptions.removeAllViews();
            addOptionToGroup(question.getOption1());
            addOptionToGroup(question.getOption2());
            addOptionToGroup(question.getOption3());
            addOptionToGroup(question.getOption4());
            addOptionToGroup(question.getOption5());

            btnSubmit.setEnabled(false);

            tvFeedback.setVisibility(View.GONE);
            btnNextQuestion.setVisibility(View.GONE);
        }
    }

    private void addOptionToGroup(String optionText) {
        if (optionText != null && !optionText.trim().isEmpty()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(optionText);
            rgOptions.addView(radioButton);
        }
    }

    private void submitAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedAnswer = selectedRadioButton.getText().toString();

        QuestionDTO currentQuestion = questions.get(currentQuestionIndex);

        if (currentQuestion.getCorrectAnswer().equals(selectedAnswer)) {
            tvFeedback.setTextAppearance(this, android.R.style.TextAppearance_Material_Medium);
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            tvFeedback.setText("Correct!");
            totalScore += currentQuestion.getNumberOfPoints();
        } else {
            tvFeedback.setTextAppearance(this, android.R.style.TextAppearance_Material_Medium);
            tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            tvFeedback.setText("Incorrect!\nCorrect answer: " + currentQuestion.getCorrectAnswer());
        }

        tvFeedback.setVisibility(View.VISIBLE);
        btnNextQuestion.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.GONE);
    }

    private void navigateToNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion(currentQuestionIndex);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            saveResult();
        }
    }

    private void saveResult() {
        UserQuizDTO result = new UserQuizDTO(0, currentUser.getId(), selectedQuiz.getId(), totalScore);
        long insertResult = userQuizUseCase.insertUserQuiz(result);
        if(insertResult <= 0) {  Toast.makeText(this, "Quiz result not saved.", Toast.LENGTH_SHORT).show(); }

        tvQuestionText.setText("Quiz Complete!");
        tvQuestionText.setTextAppearance(this, android.R.style.TextAppearance_Material_Large);
        tvQuestionText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));

        tvFeedback.setText("Your total score is: " + totalScore);
        tvFeedback.setTextAppearance(this, android.R.style.TextAppearance_Material_Medium);
        tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        tvFeedback.setVisibility(View.VISIBLE);

        rgOptions.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        btnNextQuestion.setVisibility(View.GONE);
        btnHome.setVisibility(View.VISIBLE);
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("userDTO", currentUser);
        startActivity(intent);
        finish();
    }
}