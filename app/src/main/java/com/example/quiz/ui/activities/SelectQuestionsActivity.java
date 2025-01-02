package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.QuestionRepository;
import com.example.quiz.domain.mappers.QuestionMapper;
import com.example.quiz.domain.models.QuestionDTO;
import com.example.quiz.ui.adapters.SelectableQuestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectQuestionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnSaveQuestions;
    private SelectableQuestionsAdapter adapter;
    private List<QuestionDTO> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_questions);

        initializeDependencies();
        initializeUI();
        setupListeners();
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        QuestionRepository questionRepository = new QuestionRepository(dbHelper);
        questions = QuestionMapper.toDTO(questionRepository.getAllQuestions());
    }

    private void initializeUI() {
        recyclerView = findViewById(R.id.recyclerView);
        btnSaveQuestions = findViewById(R.id.btnSaveQuestions);

        adapter = new SelectableQuestionsAdapter(questions, question -> {});
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnSaveQuestions.setOnClickListener(v -> saveSelectedQuestions());
    }

    private void saveSelectedQuestions() {
        List<QuestionDTO> selectedQuestions = adapter.getSelectedQuestions();

        if (selectedQuestions.isEmpty()) {
            Toast.makeText(this, "No questions selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, selectedQuestions.size() + " questions selected.", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra("selectedQuestions", new ArrayList<>(selectedQuestions));
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}