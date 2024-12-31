package com.example.quiz.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.domain.models.UserDTO;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<UserDTO> users;
    private OnUserClickListener listener;

    public UsersAdapter(List<UserDTO> users, OnUserClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserDTO user = users.get(position);
        holder.tvUserName.setText(user.getFirstName() + " " + user.getLastName());

        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(user));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(user.getId()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnUserClickListener {
        void onUserClick(UserDTO user);
        void onEditClick(UserDTO user);
        void onDeleteClick(int userId);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        ImageView btnEdit, btnDelete;

        public UserViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnEdit = itemView.findViewById(R.id.btnEditStudent);
            btnDelete = itemView.findViewById(R.id.btnDeleteStudent);
        }
    }
}