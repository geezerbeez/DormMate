package com.itismob.s02.group9.mco.dormmate;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.VH> {

    public interface OnConversationClick {
        void onClick(Conversation conversation);
    }

    private List<Conversation> items;
    private final OnConversationClick listener;

    public ConversationAdapter(List<Conversation> items, OnConversationClick listener) {
        this.items = items;
        this.listener = listener;
    }

    public void setItems(List<Conversation> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Conversation c = items.get(position);
        h.avatar.setText(c.getInitial());
        h.name.setText(c.getName());
        h.preview.setText(c.getLastMessage());
        h.time.setText(formatTime(c.getLastTimestamp()));

        boolean unread = c.getUnreadCount() > 0;
        h.badge.setVisibility(unread ? View.VISIBLE : View.GONE);
        h.badge.setText(String.valueOf(c.getUnreadCount()));
        h.name.setTypeface(null, unread ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

        h.itemView.setOnClickListener(v -> listener.onClick(c));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static String formatTime(long ts) {
        Date d = new Date(ts);
        if (DateUtils.isToday(ts)) {
            return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(d);
        }
        return new SimpleDateFormat("MMM d", Locale.getDefault()).format(d);
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView avatar, name, preview, time, badge;

        VH(@NonNull View v) {
            super(v);
            avatar = v.findViewById(R.id.tvAvatar);
            name = v.findViewById(R.id.tvName);
            preview = v.findViewById(R.id.tvPreview);
            time = v.findViewById(R.id.tvTime);
            badge = v.findViewById(R.id.tvBadge);
        }
    }
}
