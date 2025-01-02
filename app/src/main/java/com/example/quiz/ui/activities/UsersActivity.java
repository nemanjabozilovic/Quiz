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
import com.example.quiz.data.repositories.RoleRepository;
import com.example.quiz.data.repositories.UserRepository;
import com.example.quiz.data.repositories.UserRolesRepository;
import com.example.quiz.domain.models.UserDTO;
import com.example.quiz.domain.repositories.IRoleRepository;
import com.example.quiz.domain.repositories.IUserRepository;
import com.example.quiz.domain.repositories.IUserRolesRepository;
import com.example.quiz.domain.usecases.implementation.UserUseCase;
import com.example.quiz.ui.adapters.UsersAdapter;

public class UsersActivity extends AppCompatActivity {
    private UsersAdapter usersAdapter;
    private UserUseCase userUseCase;
    private UserDTO currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        currentUser = getIntent().getParcelableExtra("currentUser");

        if (currentUser == null) {
            showToast("No user data found");
            finish();
            return;
        }

        initializeDependencies();
        initializeUI();
        loadUsers();
    }

    private void initializeDependencies() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        IUserRepository userRepository = new UserRepository(dbHelper);
        IUserRolesRepository userRolesRepository = new UserRolesRepository(dbHelper);
        IRoleRepository roleRepository = new RoleRepository(dbHelper);

        userUseCase = new UserUseCase(userRepository, userRolesRepository, roleRepository);
    }

    private void initializeUI() {
        RecyclerView recyclerView = findViewById(R.id.rvUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersAdapter = new UsersAdapter(userUseCase.getAllUsers(), new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(UserDTO user) {
                showUserDetails(user);
            }

            @Override
            public void onEditClick(UserDTO user) {
                handleEditUser(user);
            }

            @Override
            public void onDeleteClick(int userId) {
                handleDeleteUser(userId);
            }
        });

        recyclerView.setAdapter(usersAdapter);

        if (isAdmin(currentUser)) {
            btnAddUser.setVisibility(Button.VISIBLE);
            btnAddUser.setOnClickListener(v -> openAddUserActivity());
        } else {
            btnAddUser.setVisibility(Button.GONE);
        }
    }

    private void loadUsers() {
        usersAdapter.setUsers(userUseCase.getAllUsers());
        usersAdapter.notifyDataSetChanged();
    }

    private boolean isAdmin(UserDTO user) {
        return user.getRole() != null && "Admin".equalsIgnoreCase(user.getRole().getRoleName());
    }

    private void showUserDetails(UserDTO user) {
        String userDetails = "ID: " + user.getId() + "\n" +
                "Name: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Role: " + (user.getRole() != null ? user.getRole().getRoleName() : "N/A");

        new AlertDialog.Builder(this)
                .setTitle("User Details")
                .setMessage(userDetails)
                .setPositiveButton("OK", null)
                .show();
    }

    private void handleEditUser(UserDTO user) {
        if (isAdmin(currentUser)) {
            Intent intent = new Intent(this, EditUserActivity.class);
            intent.putExtra("user", user);
            startActivityForResult(intent, 1);
        } else {
            showToast("Only admins can edit users.");
        }
    }

    private void handleDeleteUser(int userId) {
        if (userId == currentUser.getId()) {
            showToast("You cannot delete your own account.");
            return;
        }

        if (isAdmin(currentUser)) {
            confirmAndDeleteUser(userId);
        } else {
            showToast("Only admins can delete users.");
        }
    }

    private void confirmAndDeleteUser(int userId) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (userUseCase.deleteUser(userId)) {
                        showToast("User deleted successfully.");
                        loadUsers();
                    } else {
                        showToast("Error deleting user.");
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void openAddUserActivity() {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivityForResult(intent, 2);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == 1 || requestCode == 2)) {
            loadUsers();
        }
    }
}