package com.itismob.s02.group9.mco.dormmate;

public class ChatMessage {
    private final String text;
    private final boolean fromMe;
    private final long timestamp;

    public ChatMessage(String text, boolean fromMe, long timestamp) {
        this.text = text;
        this.fromMe = fromMe;
        this.timestamp = timestamp;
    }

    public String getText() { return text; }
    public boolean isFromMe() { return fromMe; }
    public long getTimestamp() { return timestamp; }
}
