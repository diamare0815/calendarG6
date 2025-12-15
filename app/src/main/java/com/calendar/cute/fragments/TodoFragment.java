package com.calendar.cute.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.R;
import com.calendar.cute.adapters.TodoAdapter;
import com.calendar.cute.dialogs.AddTodoDialog;
import com.calendar.cute.dialogs.EditTodoDialog;
import com.calendar.cute.models.TodoItem;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
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

        fabAddTodo.setOnClickListener(v -> showAddTodoDialog());
    }

    private void setupRecyclerView() {
        todoAdapter = new TodoAdapter(todoList, requireContext(),
                new TodoAdapter.OnTodoItemListener() {

                    @Override
                    public void onTodoChecked(TodoItem item, boolean isChecked) {
                        item.setCompleted(isChecked);
                        updateCounts();
                    }

                    @Override
                    public void onTodoDelete(TodoItem item) {
                        updateCounts();
                    }

                    @Override
                    public void onTodoEdit(TodoItem item) {
                        showEditTodoDialog(item);
                    }

                    @Override
                    public void onTodoStarToggle(TodoItem item, boolean isImportant) {
                        // nếu đang filter important thì refresh lại list
                        int checkedId = chipGroupFilter.getCheckedChipId();
                        if (checkedId == R.id.chip_important && !isImportant) {
                            todoAdapter.filter("important");
                        }
                    }
                });

        recyclerViewTodo.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewTodo.setAdapter(todoAdapter);

        loadSampleTodos();
        updateCounts();
    }

    private void setupFilters() {
        chipGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chip_today) {
                todoAdapter.filter("today");
            } else if (checkedId == R.id.chip_important) {
                todoAdapter.filter("important");
            } else {
                todoAdapter.filter("all");
            }
        });
    }

    private void loadSampleTodos() {
        todoList.add(new TodoItem("Complete project report", "Work", false, true, "#FFB6C1"));
        todoList.add(new TodoItem("Buy groceries", "Personal", false, false, "#FFE4B5"));
        todoList.add(new TodoItem("Call dentist", "Health", false, false, "#E0BBE4"));
        todoList.add(new TodoItem("Read 30 pages", "Personal", true, false, "#B0E0E6"));
        todoList.add(new TodoItem("Morning workout", "Health", true, true, "#98D8C8"));

        todoAdapter.filter("all");
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
        AddTodoDialog dialog = new AddTodoDialog(requireContext(), item -> {
            todoList.add(0, item);
            todoAdapter.filter("all");
            recyclerViewTodo.scrollToPosition(0);
            updateCounts();
        });
        dialog.show();
    }

    private void showEditTodoDialog(TodoItem item) {
        EditTodoDialog dialog = new EditTodoDialog(requireContext(), item, editedItem -> {
            todoAdapter.filter("all");
            updateCounts();
        });
        dialog.show();
    }
}
