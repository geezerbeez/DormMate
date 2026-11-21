package com.itismob.s02.group9.mco.dormmate;

public class Conversation {
    private static final int[] AVATAR_COLORS = {
            0xFF0099FF, 0xFFA033FF, 0xFFFF5CA1, 0xFFFF7E29, 0xFF00C6A2
    };

    private final String id;
    private final String name;
    private String lastMessage = "";
    private long lastTimestamp = 0;
    private boolean lastFromMe = false;
    private int unreadCount = 0;
    private boolean active = false;

    public Conversation(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLastMessage() { return lastMessage; }
    public long getLastTimestamp() { return lastTimestamp; }
    public boolean isLastFromMe() { return lastFromMe; }
    public int getUnreadCount() { return unreadCount; }
    public boolean isActive() { return active; }

    public String getFirstName() {
        int space = name.indexOf(' ');
        return space > 0 ? name.substring(0, space) : name;
    }

    public String getInitial() {
        return name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
    }

    public int getAvatarColor() {
        return AVATAR_COLORS[(name.hashCode() & 0x7fffffff) % AVATAR_COLORS.length];
    }

    void update(String lastMessage, long lastTimestamp, boolean fromMe) {
        this.lastMessage = lastMessage;
        this.lastTimestamp = lastTimestamp;
        this.lastFromMe = fromMe;
    }

    void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
    void setActive(boolean active) { this.active = active; }
}
