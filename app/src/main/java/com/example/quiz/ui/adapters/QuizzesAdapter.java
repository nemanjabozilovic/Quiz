package com.example.quiz.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.domain.models.QuizDTO;

import java.util.List;

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.QuizViewHolder> {
    private List<QuizDTO> quizzes;
    private final OnQuizClickListener listener;

    public QuizzesAdapter(List<QuizDTO> quizzes, OnQuizClickListener listener) {
        this.quizzes = quizzes;
        this.listener = listener;
    }

    public void setQuizzes(List<QuizDTO> quizzes) {
        this.quizzes = quizzes;
        notifyDataSetChanged();
    }

    @Override
    public QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, int position) {
        QuizDTO quiz = quizzes.get(position);
        holder.tvQuizName.setText(quiz.getQuizName());
        holder.tvQuizDate.setText(quiz.getDate());

        holder.itemView.setOnClickListener(v -> listener.onQuizClick(quiz));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(quiz));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(quiz.getId()));
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public interface OnQuizClickListener {
        void onQuizClick(QuizDTO quiz);
        void onEditClick(QuizDTO quiz);
        void onDeleteClick(int quizId);
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuizName;
        TextView tvQuizDate;
        View btnEdit, btnDelete;

        public QuizViewHolder(View itemView) {
            super(itemView);
            tvQuizName = itemView.findViewById(R.id.tvQuizName);
            tvQuizDate = itemView.findViewById(R.id.tvQuizDate);
            btnEdit = itemView.findViewById(R.id.btnEditQuiz);
            btnDelete = itemView.findViewById(R.id.btnDeleteQuiz);
        }
    }
}