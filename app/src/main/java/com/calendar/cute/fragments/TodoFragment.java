package com.calendar.cute.fragments;


import android.os.Bundle;
import com.calendar.cute.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.adapters.TodoAdapter;
import com.calendar.cute.dialogs.AddTodoDialog;
import com.calendar.cute.dialogs.EditTodoDialog;
import com.calendar.cute.models.TodoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;

public class TodoFragment extends Fragment {

    private RecyclerView recyclerViewTodo;
    private FloatingActionButton fabAddTodo;
    private TodoAdapter todoAdapter;
    private List<TodoItem> todoList;
    private ChipGroup chipGroupFilter;
    private TextView tvPendingCount, tvCompletedCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        initViews(view);
        setupRecyclerView();
        setupFilters();

        return view;
    }

    private void initViews(View view) {
        recyclerViewTodo = view.findViewById(R.id.recycler_todo);
        fabAddTodo = view.findViewById(R.id.fab_add_todo);
        chipGroupFilter = view.findViewById(R.id.chip_group_filter);
        tvPendingCount = view.findViewById(R.id.tv_pending_count);
        tvCompletedCount = view.findViewById(R.id.tv_completed_count);

        todoList = new ArrayList<>();

        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTodoDialog();
            }
        });
    }

    private void setupRecyclerView() {
        todoAdapter = new TodoAdapter(todoList, getContext(), new TodoAdapter.OnTodoItemListener() {
            @Override
            public void onTodoChecked(TodoItem item, boolean isChecked) {
                item.setCompleted(isChecked);
                updateCounts();
            }

            @Override
            public void onTodoDelete(TodoItem item) {
                todoList.remove(item);
                todoAdapter.notifyDataSetChanged();
                updateCounts();
            }

            @Override
            public void onTodoEdit(TodoItem item) {
                showEditTodoDialog(item);
            }
        });

        recyclerViewTodo.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTodo.setAdapter(todoAdapter);

        loadSampleTodos();
        updateCounts();
    }

    private void setupFilters() {
        chipGroupFilter.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                filterTodos(checkedId);
            }
        });
    }

    private void filterTodos(int checkedId) {
        if (checkedId == R.id.chip_all) {
            todoAdapter.filter("all");
        } else if (checkedId == R.id.chip_today) {
            todoAdapter.filter("today");
        } else if (checkedId == R.id.chip_week) {
            todoAdapter.filter("week");
        } else if (checkedId == R.id.chip_important) {
            todoAdapter.filter("important");
        }
    }

    private void loadSampleTodos() {
        todoList.add(new TodoItem("Complete project report", "Work", false, true, "#FFB6C1"));
        todoList.add(new TodoItem("Buy groceries", "Personal", false, false, "#FFE4B5"));
        todoList.add(new TodoItem("Call dentist", "Health", false, false, "#E0BBE4"));
        todoList.add(new TodoItem("Read 30 pages", "Personal", true, false, "#B0E0E6"));
        todoList.add(new TodoItem("Morning workout", "Health", true, true, "#98D8C8"));

        todoAdapter.notifyDataSetChanged();
    }

    private void updateCounts() {
        int pending = 0;
        int completed = 0;

        for (TodoItem item : todoList) {
            if (item.isCompleted()) {
                completed++;
            } else {
                pending++;
            }
        }

        tvPendingCount.setText(String.valueOf(pending));
        tvCompletedCount.setText(String.valueOf(completed));
    }

    private void showAddTodoDialog() {
        AddTodoDialog dialog = new AddTodoDialog(getContext(), new AddTodoDialog.OnTodoAddedListener() {
            @Override
            public void onTodoAdded(TodoItem item) {
                todoList.add(0, item);
                todoAdapter.notifyDataSetChanged();
                updateCounts();
            }
        });
        dialog.show();
    }

    private void showEditTodoDialog(TodoItem item) {
        EditTodoDialog dialog = new EditTodoDialog(getContext(), item, new EditTodoDialog.OnTodoEditedListener() {
            @Override
            public void onTodoEdited(TodoItem editedItem) {
                todoAdapter.notifyDataSetChanged();
                updateCounts();
            }
        });
        dialog.show();
    }
}