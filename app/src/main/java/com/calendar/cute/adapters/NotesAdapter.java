package com.calendar.cute.adapters;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.calendar.cute.R;
import com.calendar.cute.models.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notesList;
    private Context context;
    private OnNoteItemListener listener;

    public interface OnNoteItemListener {
        void onNoteClick(Note note);
        void onNoteDelete(Note note);
    }

    public NotesAdapter(List<Note> notesList, Context context, OnNoteItemListener listener) {
        this.notesList = notesList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesList.get(position);

        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        holder.tvDate.setText(sdf.format(new Date(note.getTimestamp())));

        try {
            holder.cardView.setCardBackgroundColor(Color.parseColor(note.getColor()));
        } catch (IllegalArgumentException e) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFB6C1"));
        }

        holder.cardView.setOnClickListener(v -> listener.onNoteClick(note));
        holder.cardView.setOnLongClickListener(v -> {
            listener.onNoteDelete(note);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvContent, tvDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_note);
            tvTitle = itemView.findViewById(R.id.tv_note_title);
            tvContent = itemView.findViewById(R.id.tv_note_content);
            tvDate = itemView.findViewById(R.id.tv_note_date);
        }
    }
}