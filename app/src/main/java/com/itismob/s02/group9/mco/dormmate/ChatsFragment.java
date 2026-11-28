package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatsFragment extends Fragment {
    private ConversationAdapter adapter;
    private ChatActiveContactAdapter activeAdapter;
    private View emptyView;
    private String query = "";

    public ChatsFragment() {
        super(R.layout.fragment_messages);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = view.findViewById(R.id.rvConversations);
        RecyclerView rvActive = view.findViewById(R.id.rvActive);
        emptyView = view.findViewById(R.id.tvEmpty);
        EditText etSearch = view.findViewById(R.id.etSearch);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ConversationAdapter(new ArrayList<>(), this::openChat);
        rv.setAdapter(adapter);

        rvActive.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        activeAdapter = new ChatActiveContactAdapter(new ArrayList<>(), this::openChat);
        rvActive.setAdapter(activeAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                query = s.toString().trim().toLowerCase(Locale.getDefault());
                refresh();
            }
        });
    }

    private void openChat(Conversation conversation) {
        Intent intent = new Intent(requireContext(), ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_CONVERSATION_ID, conversation.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(); // pick up new previews / cleared unread dots after a chat
    }

    private void refresh() {
        List<Conversation> all = ChatStore.get().getConversations();
        activeAdapter.setItems(all);

        List<Conversation> shown = new ArrayList<>();
        for (Conversation c : all) {
            if (query.isEmpty() || c.getName().toLowerCase(Locale.getDefault()).contains(query)) {
                shown.add(c);
            }
        }
        adapter.setItems(shown);
        emptyView.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
