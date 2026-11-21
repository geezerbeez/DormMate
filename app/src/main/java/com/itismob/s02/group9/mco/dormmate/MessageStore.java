package com.itismob.s02.group9.mco.dormmate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory message store with sample data. Swap the internals for Room or
 * Firebase later; the rest of the UI only talks to this class.
 */
public class MessageStore {
    private static MessageStore instance;

    private final Map<String, Conversation> conversations = new HashMap<>();
    private final Map<String, List<ChatMessage>> messages = new HashMap<>();

    public static synchronized MessageStore get() {
        if (instance == null) instance = new MessageStore();
        return instance;
    }

    private MessageStore() {
        long now = System.currentTimeMillis();
        long min = 60_000L, hour = 60 * min, day = 24 * hour;

        seed("1", "Ana Santos", 1, new Object[][]{
                {"Hi! Saw your roommate profile, are you still looking?", false, now - 3 * hour},
                {"Yes! I'm looking for a place near campus.", true, now - 2 * hour - 50 * min},
                {"Great, I have a free slot in a 2-person dorm. Want to see photos?", false, now - 2 * hour - 45 * min},
        });
        seed("2", "Miguel Dela Cruz", 0, new Object[][]{
                {"What time works for the viewing tomorrow?", false, now - day - hour},
                {"Anytime after 2 PM works for me.", true, now - day},
        });
        seed("3", "Housing Office", 2, new Object[][]{
                {"Your reservation inquiry has been received.", false, now - 3 * day},
                {"Please bring a valid ID during the visit.", false, now - 3 * day + min},
        });
        seedActive();
    }

    private void seedActive() {
        // Sample data: pretend these contacts are online.
        conversations.get("1").setActive(true);
        conversations.get("2").setActive(true);
    }

    private void seed(String id, String name, int unread, Object[][] rows) {
        Conversation c = new Conversation(id, name);
        List<ChatMessage> list = new ArrayList<>();
        for (Object[] r : rows) {
            list.add(new ChatMessage((String) r[0], (Boolean) r[1], (Long) r[2]));
        }
        ChatMessage last = list.get(list.size() - 1);
        c.update(last.getText(), last.getTimestamp(), last.isFromMe());
        c.setUnreadCount(unread);
        conversations.put(id, c);
        messages.put(id, list);
    }

    /** Conversations, most recent first. */
    public List<Conversation> getConversations() {
        List<Conversation> out = new ArrayList<>(conversations.values());
        Collections.sort(out, (a, b) -> Long.compare(b.getLastTimestamp(), a.getLastTimestamp()));
        return out;
    }

    public Conversation getConversation(String id) {
        return conversations.get(id);
    }

    /** Live list; the chat adapter notifies on changes after send(). */
    public List<ChatMessage> getMessages(String id) {
        List<ChatMessage> list = messages.get(id);
        return list != null ? list : new ArrayList<>();
    }

    public ChatMessage send(String id, String text) {
        ChatMessage m = new ChatMessage(text, true, System.currentTimeMillis());
        messages.get(id).add(m);
        conversations.get(id).update(m.getText(), m.getTimestamp(), true);
        return m;
    }

    public void markRead(String id) {
        Conversation c = conversations.get(id);
        if (c != null) c.setUnreadCount(0);
    }
}
