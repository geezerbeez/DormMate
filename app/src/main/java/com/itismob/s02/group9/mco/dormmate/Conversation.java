package com.itismob.s02.group9.mco.dormmate;

public class Conversation {
    private final String id;
    private final String name;
    private String lastMessage = "";
    private long lastTimestamp = 0;
    private int unreadCount = 0;

    public Conversation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLastMessage() { return lastMessage; }
    public long getLastTimestamp() { return lastTimestamp; }
    public int getUnreadCount() { return unreadCount; }

    public String getInitial() {
        return name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
    }

    void update(String lastMessage, long lastTimestamp) {
        this.lastMessage = lastMessage;
        this.lastTimestamp = lastTimestamp;
    }

    void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
}
