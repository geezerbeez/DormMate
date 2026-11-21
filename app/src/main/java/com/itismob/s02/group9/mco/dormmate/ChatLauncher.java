package com.itismob.s02.group9.mco.dormmate;

import android.app.Activity;
import android.content.Intent;

/** One place that opens (or creates) the chat with a roommate, so every button behaves the same. */
public final class ChatLauncher {

    private ChatLauncher() {}

    public static void openChatWith(Activity from, Roommate roommate) {
        // One chat per person, keyed by the roommate id, with their photo attached.
        Conversation chat = MessageStore.get()
                .getOrCreateConversation(roommate.id, roommate.name, roommate.photoRes);

        // Put the Messages tab underneath so Back from the chat lands there.
        Intent pages = new Intent(from, Pages.class);
        pages.putExtra(Pages.EXTRA_OPEN_TAB, "messages");
        pages.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        from.startActivity(pages);

        Intent chatIntent = new Intent(from, ChatActivity.class);
        chatIntent.putExtra(ChatActivity.EXTRA_CONVERSATION_ID, chat.getId());
        from.startActivity(chatIntent);
    }
}
