package com.itismob.s02.group9.mco.dormmate;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        boolean unread = c.getUnreadCount() > 0;

        h.avatar.setText(c.getInitial());
        h.avatar.setBackgroundTintList(ColorStateList.valueOf(c.getAvatarColor()));
        h.name.setText(c.getName());

        String msg = c.getLastMessage();
        if (msg.length() > 28) msg = msg.substring(0, 28).trim() + "\u2026";
        String prefix = c.isLastFromMe() ? "You: " : "";
        h.preview.setText(prefix + msg + " \u00B7 " + formatTime(c.getLastTimestamp()));

        int dark = ContextCompat.getColor(h.itemView.getContext(), R.color.msgr_text);
        int sub = ContextCompat.getColor(h.itemView.getContext(), R.color.msgr_sub);
        Typeface style = unread ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT;
        h.name.setTypeface(style);
        h.preview.setTypeface(style);
        h.preview.setTextColor(unread ? dark : sub);
        h.dot.setVisibility(unread ? View.VISIBLE : View.INVISIBLE);

        h.itemView.setOnClickListener(v -> listener.onClick(c));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Short form for the chat list: time today, otherwise "Nov 16". */
    static String formatTime(long ts) {
        Date d = new Date(ts);
        if (DateUtils.isToday(ts)) {
            return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(d);
        }
        return new SimpleDateFormat("MMM d", Locale.getDefault()).format(d);
    }

    /** Centered separator inside a chat: "2:15 PM" today, otherwise "Nov 16, 2:15 PM". */
    static String formatSeparator(long ts) {
        Date d = new Date(ts);
        String pattern = DateUtils.isToday(ts) ? "h:mm a" : "MMM d, h:mm a";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(d);
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView avatar, name, preview;
        final View dot;

        VH(@NonNull View v) {
            super(v);
            avatar = v.findViewById(R.id.tvAvatar);
            name = v.findViewById(R.id.tvName);
            preview = v.findViewById(R.id.tvPreview);
            dot = v.findViewById(R.id.unreadDot);
        }
    }
}
