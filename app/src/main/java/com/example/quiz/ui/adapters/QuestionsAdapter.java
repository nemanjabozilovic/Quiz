package com.example.quiz.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.domain.models.QuestionDTO;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {
    private List<QuestionDTO> questions;
    private final OnQuestionClickListener listener;

    public QuestionsAdapter(List<QuestionDTO> questions, OnQuestionClickListener listener) {
        this.questions = questions;
        this.listener = listener;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        QuestionDTO question = questions.get(position);
        holder.tvQuestionText.setText(question.getText());

        holder.itemView.setOnClickListener(v -> listener.onQuestionClick(question));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(question));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(question.getId()));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public interface OnQuestionClickListener {
        void onQuestionClick(QuestionDTO question);
        void onEditClick(QuestionDTO question);
        void onDeleteClick(int questionId);
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionText;
        View btnEdit, btnDelete;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            btnEdit = itemView.findViewById(R.id.btnEditQuestion);
            btnDelete = itemView.findViewById(R.id.btnDeleteQuestion);
        }
    }
}