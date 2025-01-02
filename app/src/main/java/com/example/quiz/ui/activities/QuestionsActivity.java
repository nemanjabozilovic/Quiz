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
import com.example.quiz.data.repositories.QuestionRepository;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.domain.usecases.implementation.QuestionUseCase;
import com.example.quiz.ui.adapters.QuestionsAdapter;

import java.util.List;

public class QuestionsActivity extends AppCompatActivity {
    private QuestionsAdapter questionsAdapter;
    private QuestionUseCase questionUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        initializeDatabase();
        setupRecyclerView();
        setupAddQuestionButton();
        loadQuestions();
    }

    private void initializeDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionUseCase = new QuestionUseCase(new QuestionRepository(dbHelper));
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionsAdapter = new QuestionsAdapter(
                questionUseCase.getAllQuestions(),
                new QuestionsAdapter.OnQuestionClickListener() {
                    @Override
                    public void onQuestionClick(QuestionDTO question) {
                        showQuestionDetailsDialog(question);
                    }

                    @Override
                    public void onEditClick(QuestionDTO question) {
                        navigateToEditQuestionActivity(question);
                    }

                    @Override
                    public void onDeleteClick(int questionId) {
                        handleDeleteQuestion(questionId);
                    }
                }
        );

        recyclerView.setAdapter(questionsAdapter);
    }

    private void setupAddQuestionButton() {
        Button btnAddQuestion = findViewById(R.id.btnAddQuestion);
        btnAddQuestion.setOnClickListener(v -> navigateToAddQuestionActivity());
    }

    private void loadQuestions() {
        List<QuestionDTO> questions = questionUseCase.getAllQuestions();
        questionsAdapter.setQuestions(questions);
    }

    private void showQuestionDetailsDialog(QuestionDTO question) {
        String questionDetails = String.format(
                "ID: %d\nQuestion: %s\nCorrect Answer: %s\nNumber of Points: %d",
                question.getId(),
                question.getText(),
                question.getCorrectAnswer(),
                question.getNumberOfPoints()
        );

        new AlertDialog.Builder(this)
                .setTitle("Question Details")
                .setMessage(questionDetails)
                .setPositiveButton("OK", null)
                .show();
    }

    private void navigateToEditQuestionActivity(QuestionDTO question) {
        Intent intent = new Intent(this, EditQuestionActivity.class);
        intent.putExtra("question", question);
        startActivityForResult(intent, 1);
    }

    private void navigateToAddQuestionActivity() {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        startActivityForResult(intent, 2);
    }

    private void handleDeleteQuestion(int questionId) {
        boolean isDeleted = questionUseCase.deleteQuestion(questionId);

        if (isDeleted) {
            showToast("Question deleted successfully.");
            loadQuestions();
        } else {
            showToast("Error deleting question.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            loadQuestions();
        }
    }
}