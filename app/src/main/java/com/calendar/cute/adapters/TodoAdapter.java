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

    private List<TodoItem> todoList;
    private List<TodoItem> todoListFiltered;
    private Context context;
    private OnTodoItemListener listener;

    public interface OnTodoItemListener {
        void onTodoChecked(TodoItem item, boolean isChecked);
        void onTodoDelete(TodoItem item);
        void onTodoEdit(TodoItem item);
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

        // Show/hide important icon
        holder.ivImportant.setVisibility(item.isImportant() ? View.VISIBLE : View.GONE);

        // Strikethrough if completed
        if (item.isCompleted()) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setAlpha(0.5f);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvTitle.setAlpha(1.0f);
        }

        // Set color
        try {
            holder.colorIndicator.setBackgroundColor(Color.parseColor(item.getColor()));
        } catch (IllegalArgumentException e) {
            holder.colorIndicator.setBackgroundColor(Color.parseColor("#FFB6C1"));
        }

        // Checkbox listener
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTodoChecked(item, holder.checkBox.isChecked());
                notifyItemChanged(position);
            }
        });

        // Delete listener
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTodoDelete(item);
            }
        });

        // Edit listener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTodoEdit(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoListFiltered.size();
    }

    public void filter(String filterType) {
        todoListFiltered.clear();

        if (filterType.equals("all")) {
            todoListFiltered.addAll(todoList);
        } else if (filterType.equals("today")) {
            // Filter today's tasks
            for (TodoItem item : todoList) {
                if (!item.isCompleted()) {
                    todoListFiltered.add(item);
                }
            }
        } else if (filterType.equals("important")) {
            for (TodoItem item : todoList) {
                if (item.isImportant()) {
                    todoListFiltered.add(item);
                }
            }
        } else {
            todoListFiltered.addAll(todoList);
        }

        notifyDataSetChanged();
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