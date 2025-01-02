package com.example.quiz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.R;
import com.example.quiz.data.datasources.databases.DatabaseHelper;
import com.example.quiz.data.repositories.RoleRepository;
import com.example.quiz.data.repositories.UserRepository;
import com.example.quiz.data.repositories.UserRolesRepository;
import com.example.quiz.domain.models.RoleDTO;
import com.example.quiz.domain.models.UserDTO;
import com.example.quiz.domain.usecases.implementation.RoleUseCase;
import com.example.quiz.domain.usecases.implementation.UserRolesUseCase;
import com.example.quiz.domain.usecases.implementation.UserUseCase;
import com.example.quiz.domain.usecases.interfaces.IRoleUseCase;
import com.example.quiz.domain.usecases.interfaces.IUserRolesUseCase;
import com.example.quiz.domain.usecases.interfaces.IUserUseCase;

import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity {
    private EditText etFirstName, etLastName, etEmail, etPassword;
    private Spinner spinnerRole;
    private Button btnSave;
    private IUserUseCase userUseCase;
    private IUserRolesUseCase userRolesUseCase;
    private List<RoleDTO> rolesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        initializeUIElements();
        initializeDependencies();
        loadRolesIntoSpinner();
        setupListeners();
    }

    private void initializeUIElements() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnSave = findViewById(R.id.btnSave);
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userUseCase = new UserUseCase(
                new UserRepository(dbHelper),
                new UserRolesRepository(dbHelper),
                new RoleRepository(dbHelper)
        );
        IRoleUseCase roleUseCase = new RoleUseCase(new RoleRepository(dbHelper));
        userRolesUseCase = new UserRolesUseCase(
                new UserRolesRepository(dbHelper),
                new RoleRepository(dbHelper)
        );

        rolesList = roleUseCase.getAllRoles();
        if (rolesList.isEmpty()) {
            Toast.makeText(this, "No roles available. Please add roles first.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupListeners() {
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveUser();
            }
        });
    }

    private void loadRolesIntoSpinner() {
        List<String> roleNames = new ArrayList<>();
        for (RoleDTO role : rolesList) {
            roleNames.add(role.getRoleName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    private boolean validateInputs() {
        if (etFirstName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "First name is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etLastName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Last name is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUser() {
        UserDTO newUserDTO = createUserDTO();
        boolean isInserted = userUseCase.insertUser(newUserDTO);

        if (isInserted) {
            assignRoleToUser(newUserDTO);
            navigateBackWithSuccess(newUserDTO);
        } else {
            Toast.makeText(this, "Error adding user. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void assignRoleToUser(UserDTO user) {
        int selectedRolePosition = spinnerRole.getSelectedItemPosition();
        int selectedRoleId = rolesList.get(selectedRolePosition).getId();
        userRolesUseCase.assignRoleToUser(user.getId(), selectedRoleId);
    }

    private void navigateBackWithSuccess(UserDTO user) {
        Toast.makeText(this, "User added successfully.", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newUser", user);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private UserDTO createUserDTO() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        RoleDTO selectedRole = rolesList.get(spinnerRole.getSelectedItemPosition());

        return new UserDTO(0, firstName, lastName, email, password, selectedRole);
    }
}