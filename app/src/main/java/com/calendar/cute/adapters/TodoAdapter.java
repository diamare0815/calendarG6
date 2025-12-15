package com.calendar.cute.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.R;
import com.calendar.cute.models.TodoItem;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private final List<TodoItem> todoList;          // list gốc
    private final List<TodoItem> todoListFiltered;  // list hiển thị
    private final Context context;
    private final OnTodoItemListener listener;

    public interface OnTodoItemListener {
        void onTodoChecked(TodoItem item, boolean isChecked);
        void onTodoDelete(TodoItem item);
        void onTodoEdit(TodoItem item);
        void onTodoStarToggle(TodoItem item, boolean isImportant);
    }

    public TodoAdapter(List<TodoItem> todoList, Context context, OnTodoItemListener listener) {
        this.todoList = todoList;
        this.todoListFiltered = new ArrayList<>(todoList);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem item = todoListFiltered.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvCategory.setText(item.getCategory());
        holder.checkBox.setChecked(item.isCompleted());

        updateStarIcon(holder.ivImportant, item.isImportant());

        // strike text nếu completed
        if (item.isCompleted()) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setAlpha(0.5f);
            holder.tvCategory.setAlpha(0.5f);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvTitle.setAlpha(1f);
            holder.tvCategory.setAlpha(1f);
        }

        // màu
        try {
            holder.colorIndicator.setBackgroundColor(Color.parseColor(item.getColor()));
        } catch (Exception e) {
            holder.colorIndicator.setBackgroundColor(Color.parseColor("#FFB6C1"));
        }

        /* ===== CHECKBOX ===== */
        holder.checkBox.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            boolean checked = holder.checkBox.isChecked();
            item.setCompleted(checked);
            listener.onTodoChecked(item, checked);

            notifyItemChanged(pos);
        });

        /* ===== DELETE ===== */
        holder.ivDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            removeAt(pos);
            listener.onTodoDelete(item);
        });

        /* ===== STAR ===== */
        holder.ivImportant.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            boolean newStatus = !item.isImportant();
            item.setImportant(newStatus);
            updateStarIcon(holder.ivImportant, newStatus);

            notifyItemChanged(pos);
            listener.onTodoStarToggle(item, newStatus);
        });

        /* ===== EDIT ===== */
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            listener.onTodoEdit(item);
        });
    }

    @Override
    public int getItemCount() {
        return todoListFiltered.size();
    }

    /* ===== XÓA AN TOÀN ===== */
    private void removeAt(int position) {
        TodoItem removed = todoListFiltered.get(position);

        todoListFiltered.remove(position);
        todoList.remove(removed);

        notifyItemRemoved(position);
    }

    /* ===== FILTER ===== */
    public void filter(String type) {
        todoListFiltered.clear();

        switch (type) {
            case "today":
                for (TodoItem item : todoList) {
                    if (!item.isCompleted()) {
                        todoListFiltered.add(item);
                    }
                }
                break;

            case "important":
                for (TodoItem item : todoList) {
                    if (item.isImportant()) {
                        todoListFiltered.add(item);
                    }
                }
                break;

            case "all":
            default:
                todoListFiltered.addAll(todoList);
                break;
        }

        notifyDataSetChanged();
    }

    /* ===== STAR ICON ===== */
    private void updateStarIcon(ImageView iv, boolean important) {
        if (important) {
            iv.setImageResource(android.R.drawable.star_big_on);
            iv.setColorFilter(Color.parseColor("#FFD700"));
        } else {
            iv.setImageResource(android.R.drawable.star_big_off);
            iv.setColorFilter(Color.parseColor("#CCCCCC"));
        }
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CheckBox checkBox;
        TextView tvTitle, tvCategory;
        ImageView ivImportant, ivDelete;
        View colorIndicator;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_todo);
            checkBox = itemView.findViewById(R.id.checkbox_todo);
            tvTitle = itemView.findViewById(R.id.tv_todo_title);
            tvCategory = itemView.findViewById(R.id.tv_todo_category);
            ivImportant = itemView.findViewById(R.id.iv_important);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            colorIndicator = itemView.findViewById(R.id.color_indicator);
        }
    }
}
