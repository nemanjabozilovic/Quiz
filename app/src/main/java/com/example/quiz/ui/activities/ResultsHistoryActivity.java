package com.example.quiz.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.QuestionQuizRepository;
import com.example.quiz.data.repositories.QuizRepository;
import com.example.quiz.data.repositories.RoleRepository;
import com.example.quiz.data.repositories.UserQuizRepository;
import com.example.quiz.data.repositories.UserRepository;
import com.example.quiz.data.repositories.UserRolesRepository;
import com.example.quiz.domain.models.UserDTO;
import com.example.quiz.domain.models.UserQuizDTO;
import com.example.quiz.domain.usecases.implementation.QuizUseCase;
import com.example.quiz.domain.usecases.implementation.UserQuizUseCase;
import com.example.quiz.domain.usecases.implementation.UserUseCase;
import com.example.quiz.ui.adapters.ResultsAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultsHistoryActivity extends AppCompatActivity {
    private RecyclerView resultsRecyclerView;
    private Spinner sortOptionsSpinner;

    private UserQuizUseCase userQuizUseCase;
    private UserUseCase userUseCase;
    private QuizUseCase quizUseCase;

    private List<UserQuizDTO> userQuizList;
    private ResultsAdapter resultsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_history);

        initializeDependencies();
        initializeUI();
        setupListeners();
        fetchResults();
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userQuizUseCase = new UserQuizUseCase(new UserQuizRepository(dbHelper));
        userUseCase = new UserUseCase(new UserRepository(dbHelper), new UserRolesRepository(dbHelper), new RoleRepository(dbHelper));
        quizUseCase = new QuizUseCase(new QuizRepository(dbHelper), new QuestionQuizRepository(dbHelper));
    }

    private void initializeUI() {
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);
        sortOptionsSpinner = findViewById(R.id.sortOptionsSpinner);

        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.sort_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortOptionsSpinner.setAdapter(adapter);
    }

    private void setupListeners() {
        sortOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortResults(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchResults() {
        userQuizList = userQuizUseCase.getAllUserQuizzes();

        for (UserQuizDTO userQuiz : userQuizList) {
            UserDTO user = userUseCase.getUserById(userQuiz.getUserId());
            if (user != null) {
                userQuiz.setUserName(user.getFirstName() + " " + user.getLastName());
            }

            userQuiz.setQuizName(quizUseCase.getQuizById(userQuiz.getQuizId()).getQuizName());
        }

        resultsAdapter = new ResultsAdapter(userQuizList);
        resultsRecyclerView.setAdapter(resultsAdapter);
    }

    private void sortResults(int sortOption) {
        if (userQuizList == null) return;

        switch (sortOption) {
            case 0:
                Collections.sort(userQuizList, Comparator.comparing(UserQuizDTO::getUserId));
                break;
            case 1:
                Collections.sort(userQuizList, Comparator.comparing(UserQuizDTO::getQuizId));
                break;
            case 2:
                Collections.sort(userQuizList, Comparator.comparingInt(UserQuizDTO::getTotalNumberOfPoints).reversed());
                break;
        }

        resultsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchResults();
    }
}