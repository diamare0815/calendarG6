package com.calendar.cute.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import com.calendar.cute.R;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.adapters.NotesAdapter;
import com.calendar.cute.dialogs.AddNoteDialog;
import com.calendar.cute.dialogs.EditNoteDialog;
import com.calendar.cute.models.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton fabAddNote;
    private NotesAdapter notesAdapter;
    private List<Note> notesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        initViews(view);
        setupRecyclerView();

        return view;
    }

    private void initViews(View view) {
        recyclerViewNotes = view.findViewById(R.id.recycler_notes);
        fabAddNote = view.findViewById(R.id.fab_add_note);

        notesList = new ArrayList<>();

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog();
            }
        });
    }

    private void setupRecyclerView() {
        notesAdapter = new NotesAdapter(notesList, getContext(), new NotesAdapter.OnNoteItemListener() {
            @Override
            public void onNoteClick(Note note) {
                showEditNoteDialog(note);
            }

            @Override
            public void onNoteDelete(Note note) {
                notesList.remove(note);
                notesAdapter.notifyDataSetChanged();
            }
        });

        recyclerViewNotes.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewNotes.setAdapter(notesAdapter);

        loadSampleNotes();
    }

    private void loadSampleNotes() {
        notesList.add(new Note("Shopping List", "Milk, Eggs, Bread, Coffee", "#FFB6C1", System.currentTimeMillis()));
        notesList.add(new Note("Meeting Notes", "Discuss Q4 targets and team goals", "#FFE4B5", System.currentTimeMillis()));
        notesList.add(new Note("Ideas", "New app features: dark mode, widgets", "#E0BBE4", System.currentTimeMillis()));
        notesList.add(new Note("Travel Plans", "Japan trip - March 2025", "#B0E0E6", System.currentTimeMillis()));
        notesList.add(new Note("Recipe", "Chocolate cake ingredients and steps", "#98D8C8", System.currentTimeMillis()));
        notesList.add(new Note("Books to Read", "1984, Sapiens, Atomic Habits", "#F0E68C", System.currentTimeMillis()));

        notesAdapter.notifyDataSetChanged();
    }

    private void showAddNoteDialog() {
        AddNoteDialog dialog = new AddNoteDialog(getContext(), new AddNoteDialog.OnNoteAddedListener() {
            @Override
            public void onNoteAdded(Note note) {
                notesList.add(0, note);
                notesAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    private void showEditNoteDialog(Note note) {
        EditNoteDialog dialog = new EditNoteDialog(getContext(), note, new EditNoteDialog.OnNoteEditedListener() {
            @Override
            public void onNoteEdited(Note editedNote) {
                notesAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }
}