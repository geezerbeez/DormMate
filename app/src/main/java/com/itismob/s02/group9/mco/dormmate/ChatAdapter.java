package com.itismob.s02.group9.mco.dormmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {
    private static final int TYPE_SENT = 0;
    private static final int TYPE_RECEIVED = 1;
    private static final long SEPARATOR_GAP_MS = 15 * 60 * 1000L;

    private final List<ChatMessage> items;

    public ChatAdapter(List<ChatMessage> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).isFromMe() ? TYPE_SENT : TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == TYPE_SENT ? R.layout.item_message_sent : R.layout.item_message_received;
        return new VH(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        ChatMessage m = items.get(position);
        h.text.setText(m.getText());


        boolean showTime = position == 0
                || m.getTimestamp() - items.get(position - 1).getTimestamp() > SEPARATOR_GAP_MS;
        h.time.setVisibility(showTime ? View.VISIBLE : View.GONE);
        if (showTime) h.time.setText(ConversationAdapter.formatSeparator(m.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView text, time;

        VH(@NonNull View v) {
            super(v);
            text = v.findViewById(R.id.tvMessage);
            time = v.findViewById(R.id.tvMessageTime);
        }
    }
}
