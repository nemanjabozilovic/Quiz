package com.example.quiz.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.domain.models.UserQuizDTO;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    private final List<UserQuizDTO> resultsList;

    public ResultsAdapter(List<UserQuizDTO> resultsList) {
        this.resultsList = resultsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserQuizDTO result = resultsList.get(position);

        holder.userNameTextView.setText(result.getUserName());
        holder.quizNameTextView.setText(result.getQuizName());
        holder.totalPointsTextView.setText(String.valueOf(result.getTotalNumberOfPoints()));
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, quizNameTextView, totalPointsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            quizNameTextView = itemView.findViewById(R.id.quizNameTextView);
            totalPointsTextView = itemView.findViewById(R.id.totalPointsTextView);
        }
    }
}