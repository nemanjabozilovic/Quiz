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
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.rvUsers);
        Button btnAddUser = findViewById(R.id.btnAddUser);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        IUserRepository userRepository = new UserRepository(dbHelper);
        IUserRolesRepository userRolesRepository = new UserRolesRepository(dbHelper);
        IRoleRepository roleRepository = new RoleRepository(dbHelper);

        userUseCase = new UserUseCase(userRepository, userRolesRepository, roleRepository);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersAdapter = new UsersAdapter(userUseCase.getAllUsers(), new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(UserDTO user) {
                String userDetails = "ID: " + user.getId() + "\n" +
                        "Name: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                        "Email: " + user.getEmail() + "\n" +
                        "Role: " + (user.getRole() != null ? user.getRole().getRoleName() : "N/A");

                new AlertDialog.Builder(UsersActivity.this)
                        .setTitle("User Details")
                        .setMessage(userDetails)
                        .setPositiveButton("OK", null)
                        .show();
            }

            @Override
            public void onEditClick(UserDTO user) {
                if (isAdmin(currentUser)) {
                    Intent intent = new Intent(UsersActivity.this, EditUserActivity.class);
                    intent.putExtra("user", user);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(UsersActivity.this, "Only admins can edit users.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick(int userId) {
                if (userId == currentUser.getId()) {
                    Toast.makeText(UsersActivity.this, "You cannot delete your own account.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isAdmin(currentUser)) {
                    if (userUseCase.deleteUser(userId)) {
                        Toast.makeText(UsersActivity.this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
                        loadUsers();
                    } else {
                        Toast.makeText(UsersActivity.this, "Error deleting user.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UsersActivity.this, "Only admins can delete users.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(usersAdapter);

        loadUsers();

        if (isAdmin(currentUser)) {
            btnAddUser.setVisibility(Button.VISIBLE);
            btnAddUser.setOnClickListener(v -> {
                Intent intent = new Intent(UsersActivity.this, AddUserActivity.class);
                startActivityForResult(intent, 2);
            });
        } else {
            btnAddUser.setVisibility(Button.GONE);
        }
    }

    private boolean isAdmin(UserDTO user) {
        return user.getRole() != null && "Admin".equalsIgnoreCase(user.getRole().getRoleName());
    }

    private void loadUsers() {
        usersAdapter.setUsers(userUseCase.getAllUsers());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1 || requestCode == 2) {
                loadUsers();
            }
        }
    }
}