package com.myown.tg.tgmainlbot.model;

public class ActiveChat {
    private long chatId;
    private String tgLogin;

    public ActiveChat(long chatId, String tgLogin) {
        this.chatId = chatId;
        this.tgLogin = tgLogin;
    }

    public long getChatId() {
        return chatId;
    }

    public String getTgLogin() {
        return tgLogin;
    }
}
