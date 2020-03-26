package com.example.visualmed;

public class ChatMessage {
    private boolean isMine;
    private String content;

    public ChatMessage(String message, boolean mine) {
        content = message;
        isMine = mine;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

}
