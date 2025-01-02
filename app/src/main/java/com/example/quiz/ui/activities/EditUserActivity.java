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

public class EditUserActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail;
    private Spinner spinnerRole;
    private UserDTO userDTO;
    private IUserUseCase userUseCase;
    private IUserRolesUseCase userRolesUseCase;
    private List<RoleDTO> rolesList = new ArrayList<>();

    private String originalFirstName, originalLastName, originalEmail, originalPassword;
    private int originalRoleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        initializeUI();
        initializeDependencies();

        if (!loadRoles()) {
            showToast("No roles available. Please add roles first.");
            finish();
            return;
        }

        loadRolesIntoSpinner();
        if (!loadUserFromIntent()) {
            showToast("Error: User data not found.");
            finish();
            return;
        }

        setupSaveButtonListener();
    }

    private void initializeUI() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        spinnerRole = findViewById(R.id.spinnerRole);
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        userUseCase = new UserUseCase(
                new UserRepository(dbHelper),
                new UserRolesRepository(dbHelper),
                new RoleRepository(dbHelper)
        );
        userRolesUseCase = new UserRolesUseCase(
                new UserRolesRepository(dbHelper),
                new RoleRepository(dbHelper)
        );
    }

    private boolean loadRoles() {
        IRoleUseCase roleUseCase = new RoleUseCase(new RoleRepository(new DatabaseHelper(this)));
        rolesList = roleUseCase.getAllRoles();
        return !rolesList.isEmpty();
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

    private boolean loadUserFromIntent() {
        userDTO = getIntent().getParcelableExtra("user");
        if (userDTO != null) {
            populateUserData();
            return true;
        }
        return false;
    }

    private void populateUserData() {
        etFirstName.setText(userDTO.getFirstName());
        etLastName.setText(userDTO.getLastName());
        etEmail.setText(userDTO.getEmail());

        originalFirstName = userDTO.getFirstName();
        originalLastName = userDTO.getLastName();
        originalEmail = userDTO.getEmail();
        originalPassword = userDTO.getPassword();
        originalRoleId = userDTO.getRole() != null ? userDTO.getRole().getId() : -1;

        if (userDTO.getRole() != null) {
            for (int i = 0; i < rolesList.size(); i++) {
                if (rolesList.get(i).getId() == userDTO.getRole().getId()) {
                    spinnerRole.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupSaveButtonListener() {
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            if (validateInputs() && hasChanges()) {
                updateUserDetails();
                if (saveUserUpdates()) {
                    showToast("User updated successfully.");
                    returnUpdatedUser();
                } else {
                    showToast("Error updating user. Please try again.");
                }
            }
        });
    }

    private boolean validateInputs() {
        if (etFirstName.getText().toString().trim().isEmpty()) {
            showToast("First name is required.");
            return false;
        }
        if (etLastName.getText().toString().trim().isEmpty()) {
            showToast("Last name is required.");
            return false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            showToast("Email is required.");
            return false;
        }
        return true;
    }

    private boolean hasChanges() {
        String newFirstName = etFirstName.getText().toString().trim();
        String newLastName = etLastName.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();
        int selectedRoleId = rolesList.get(spinnerRole.getSelectedItemPosition()).getId();

        if (!newFirstName.equals(originalFirstName) ||
                !newLastName.equals(originalLastName) ||
                !newEmail.equals(originalEmail) ||
                selectedRoleId != originalRoleId) {
            return true;
        }

        showToast("No changes detected.");
        return false;
    }

    private void updateUserDetails() {
        userDTO.setFirstName(etFirstName.getText().toString().trim());
        userDTO.setLastName(etLastName.getText().toString().trim());
        userDTO.setEmail(etEmail.getText().toString().trim());
        userDTO.setPassword(originalPassword);

        int selectedRolePosition = spinnerRole.getSelectedItemPosition();
        RoleDTO selectedRole = rolesList.get(selectedRolePosition);
        userDTO.setRole(selectedRole);
    }

    private boolean saveUserUpdates() {
        boolean isUpdated = userUseCase.updateUser(userDTO);

        if (isUpdated) {
            int selectedRoleId = rolesList.get(spinnerRole.getSelectedItemPosition()).getId();
            if (selectedRoleId != originalRoleId) {
                userRolesUseCase.assignRoleToUser(userDTO.getId(), selectedRoleId);
            }
        }

        return isUpdated;
    }

    private void returnUpdatedUser() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedUser", userDTO);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}