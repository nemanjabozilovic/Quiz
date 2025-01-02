package com.example.quiz.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.R;
import com.example.quiz.domain.models.QuestionDTO;

import java.util.ArrayList;
import java.util.List;

public class SelectableQuestionsAdapter extends RecyclerView.Adapter<SelectableQuestionsAdapter.QuestionViewHolder> {
    private final List<QuestionDTO> questions = new ArrayList<>();
    private final OnQuestionSelectListener listener;

    public SelectableQuestionsAdapter(List<QuestionDTO> questions, OnQuestionSelectListener listener) {
        this.listener = listener;
        if (questions != null) {
            this.questions.addAll(questions);
        }
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selectable_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        QuestionDTO question = questions.get(position);

        holder.tvQuestionText.setText(question.getText());
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(question.isSelected());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            question.setSelected(isChecked);
            listener.onQuestionSelect(question);
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public List<QuestionDTO> getSelectedQuestions() {
        List<QuestionDTO> selectedQuestions = new ArrayList<>();
        for (QuestionDTO question : questions) {
            if (question.isSelected()) {
                selectedQuestions.add(question);
            }
        }
        return selectedQuestions;
    }

    public interface OnQuestionSelectListener {
        void onQuestionSelect(QuestionDTO question);
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvQuestionText;
        private final CheckBox checkBox;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}