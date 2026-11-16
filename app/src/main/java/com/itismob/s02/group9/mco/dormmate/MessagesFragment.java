package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessagesFragment extends Fragment {
    private ConversationAdapter adapter;
    private View emptyView;

    public MessagesFragment() {
        super(R.layout.fragment_messages);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rvConversations);
        emptyView = view.findViewById(R.id.tvEmpty);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ConversationAdapter(MessageStore.get().getConversations(), conversation -> {
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_CONVERSATION_ID, conversation.getId());
            startActivity(intent);
        });
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh previews / unread badges after returning from a chat.
        List<Conversation> latest = MessageStore.get().getConversations();
        adapter.setItems(latest);
        emptyView.setVisibility(latest.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
