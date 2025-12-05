package com.calendar.cute.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.calendar.cute.R;
import com.calendar.cute.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private Context context;

    public interface OnEventActionListener {
        void onEdit(Event event, int position);
        void onDelete(Event event, int position);
    }

    private OnEventActionListener actionListener;

    public void setOnEventActionListener(OnEventActionListener listener) {
        this.actionListener = listener;
    }

    public EventAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.tvTitle.setText(event.getTitle());
        holder.tvTime.setText(event.getTime());

        try {
            holder.cardView.setCardBackgroundColor(Color.parseColor(event.getColor()));
        } catch (Exception e) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFB6C1"));
        }

        // Nhấn đúp để hiện menu sửa/xóa
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private long lastClickTime = 0;

            @Override
            public void onClick(View v) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 300) { // nhấn đúp <300ms
                    if(actionListener != null){
                        actionListener.onEdit(event, holder.getAdapterPosition());
                    }
                }
                lastClickTime = clickTime;
            }
        });

        // Nhấn giữ để hiện menu delete/edit
        holder.itemView.setOnLongClickListener(v -> {
            if(actionListener != null){
                showEventMenu(v, event, holder.getAdapterPosition());
            }
            return true;
        });
    }

    private void showEventMenu(View anchor, Event event, int position) {
        if (actionListener == null || anchor == null) return;

        PopupMenu popup = new PopupMenu(anchor.getContext(), anchor);
        popup.getMenuInflater().inflate(R.menu.menu_event_item, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.action_edit) {
                actionListener.onEdit(event, position);
                return true;
            } else if (id == R.id.action_delete) {
                actionListener.onDelete(event, position);
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return eventList != null ? eventList.size() : 0;
    }

    public void updateEvents(List<Event> newEvents) {
        this.eventList = newEvents != null ? newEvents : null;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle, tvTime;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_event);
            tvTitle = itemView.findViewById(R.id.tv_event_title);
            tvTime = itemView.findViewById(R.id.tv_event_time);
        }
    }
}
