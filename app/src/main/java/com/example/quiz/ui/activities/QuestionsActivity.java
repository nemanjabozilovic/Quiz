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

        RecyclerView recyclerView = findViewById(R.id.rvQuestions);
        Button btnAddQuestion = findViewById(R.id.btnAddQuestion);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        questionUseCase = new QuestionUseCase(new QuestionRepository(dbHelper));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionsAdapter = new QuestionsAdapter(questionUseCase.getAllQuestions(), new QuestionsAdapter.OnQuestionClickListener() {
            @Override
            public void onQuestionClick(QuestionDTO question) {
                String questionDetails = "ID: " + question.getId() + "\n" +
                        "Question: " + question.getText() + "\n" +
                        "Correct Answer: " + question.getCorrectAnswer() + "\n" +
                        "Number of Points: " + question.getNumberOfPoints();

                new AlertDialog.Builder(QuestionsActivity.this)
                        .setTitle("Question Details")
                        .setMessage(questionDetails)
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onEditClick(QuestionDTO question) {
                Intent intent = new Intent(QuestionsActivity.this, EditQuestionActivity.class);
                intent.putExtra("question", question);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onDeleteClick(int questionId) {
                if (questionUseCase.deleteQuestion(questionId)) {
                    Toast.makeText(QuestionsActivity.this, "Question deleted successfully.", Toast.LENGTH_SHORT).show();
                    loadQuestions();
                } else {
                    Toast.makeText(QuestionsActivity.this, "Error deleting question.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(questionsAdapter);

        loadQuestions();

        btnAddQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(QuestionsActivity.this, AddQuestionActivity.class);
            startActivityForResult(intent, 2);
        });
    }

    private void loadQuestions() {
        List<QuestionDTO> questions = questionUseCase.getAllQuestions();
        questionsAdapter.setQuestions(questions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            loadQuestions();
        }
    }
}