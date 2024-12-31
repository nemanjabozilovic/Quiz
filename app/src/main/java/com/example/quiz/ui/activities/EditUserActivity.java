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

    private String originalFirstName, originalLastName, originalEmail;
    private int originalRoleId;
    private String originalPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        spinnerRole = findViewById(R.id.spinnerRole);
        Button btnSave = findViewById(R.id.btnSave);

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
            return;
        }
        loadRolesIntoSpinner();

        userDTO = getIntent().getParcelableExtra("user");
        if (userDTO != null) {
            loadUserData(userDTO);
        } else {
            Toast.makeText(this, "Error: User data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                boolean isUpdated = false;
                updateUserDetails();

                if (!userDTO.getFirstName().equals(originalFirstName) ||
                        !userDTO.getLastName().equals(originalLastName) ||
                        !userDTO.getEmail().equals(originalEmail) ||
                        (userDTO.getRole() != null && userDTO.getRole().getId() != originalRoleId)) {

                    isUpdated = userUseCase.updateUser(userDTO);

                    if (isUpdated) {
                        int selectedRolePosition = spinnerRole.getSelectedItemPosition();
                        int selectedRoleId = rolesList.get(selectedRolePosition).getId();
                        if (originalRoleId != selectedRoleId) {
                            userRolesUseCase.assignRoleToUser(userDTO.getId(), selectedRoleId);
                        }

                        Toast.makeText(EditUserActivity.this, "User updated successfully.", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedUser", userDTO);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(EditUserActivity.this, "Error updating user. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditUserActivity.this, "No changes detected.", Toast.LENGTH_SHORT).show();
                }
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

    private void loadUserData(UserDTO user) {
        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());

        if (user.getRole() != null) {
            for (int i = 0; i < rolesList.size(); i++) {
                if (rolesList.get(i).getId() == user.getRole().getId()) {
                    spinnerRole.setSelection(i);
                    break;
                }
            }
        }

        originalFirstName = user.getFirstName();
        originalLastName = user.getLastName();
        originalEmail = user.getEmail();
        originalPassword = user.getPassword();
        originalRoleId = user.getRole() != null ? user.getRole().getId() : -1;
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
        return true;
    }

    private void updateUserDetails() {
        if (!etFirstName.getText().toString().trim().equals(originalFirstName)) {
            userDTO.setFirstName(etFirstName.getText().toString().trim());
        }
        if (!etLastName.getText().toString().trim().equals(originalLastName)) {
            userDTO.setLastName(etLastName.getText().toString().trim());
        }
        if (!etEmail.getText().toString().trim().equals(originalEmail)) {
            userDTO.setEmail(etEmail.getText().toString().trim());
        }

        int selectedRolePosition = spinnerRole.getSelectedItemPosition();
        RoleDTO selectedRole = rolesList.get(selectedRolePosition);
        if (selectedRole.getId() != originalRoleId) {
            userDTO.setRole(selectedRole);
        }

        userDTO.setPassword(originalPassword);
    }
}