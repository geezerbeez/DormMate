package com.itismob.s02.group9.mco.dormmate;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_CONVERSATION_ID = "conversation_id";

    private MessageStore store;
    private String conversationId;
    private ChatAdapter adapter;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        // Include IME insets so the input bar rides above the keyboard.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chatRoot), (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        store = MessageStore.get();
        conversationId = getIntent().getStringExtra(EXTRA_CONVERSATION_ID);
        Conversation conversation = conversationId == null ? null : store.getConversation(conversationId);
        if (conversation == null) {
            finish();
            return;
        }
        store.markRead(conversationId);

        ((TextView) findViewById(R.id.tvChatName)).setText(conversation.getName());
        ((TextView) findViewById(R.id.tvChatAvatar)).setText(conversation.getInitial());
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rvMessages = findViewById(R.id.rvMessages);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        rvMessages.setLayoutManager(lm);
        adapter = new ChatAdapter(store.getMessages(conversationId));
        rvMessages.setAdapter(adapter);
        scrollToBottom();

        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        updateSendEnabled();
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { updateSendEnabled(); }
        });
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) return;
        store.send(conversationId, text);
        etMessage.setText("");
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        scrollToBottom();
    }

    private void updateSendEnabled() {
        boolean enabled = !etMessage.getText().toString().trim().isEmpty();
        btnSend.setEnabled(enabled);
        btnSend.setAlpha(enabled ? 1f : 0.4f);
    }

    private void scrollToBottom() {
        int last = adapter.getItemCount() - 1;
        if (last >= 0) rvMessages.scrollToPosition(last);
    }
}
