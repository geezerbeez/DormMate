package com.itismob.s02.group9.mco.dormmate;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** Horizontal row of circular contact avatars shown above the chat list. */
public class ActiveContactAdapter extends RecyclerView.Adapter<ActiveContactAdapter.VH> {

    private List<Conversation> items;
    private final ConversationAdapter.OnConversationClick listener;

    public ActiveContactAdapter(List<Conversation> items, ConversationAdapter.OnConversationClick listener) {
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
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Conversation c = items.get(position);
        h.avatar.setText(c.getInitial());
        h.avatar.setBackgroundTintList(ColorStateList.valueOf(c.getAvatarColor()));
        h.name.setText(c.getFirstName());
        h.activeDot.setVisibility(c.isActive() ? View.VISIBLE : View.GONE);
        h.itemView.setOnClickListener(v -> listener.onClick(c));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView avatar, name;
        final View activeDot;

        VH(@NonNull View v) {
            super(v);
            avatar = v.findViewById(R.id.tvActiveAvatar);
            name = v.findViewById(R.id.tvActiveName);
            activeDot = v.findViewById(R.id.activeDot);
        }
    }
}
