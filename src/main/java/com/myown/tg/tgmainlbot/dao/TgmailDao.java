package com.myown.tg.tgmainlbot.dao;

import com.myown.tg.tgmainlbot.model.ActiveChat;
import com.myown.tg.tgmainlbot.model.EmailProperties;

import java.util.List;
import java.util.Set;

public interface TgmailDao {

    void addNewUser(String login, long chatId, String username);

    void addMailProperties(String login, String host, String email, String password);

    List<EmailProperties> getMailPropertiesByUser(String login);

    void addActiveChat(long chatId, String login);

    void deleteActiveChat(long chatId, String login);

    void deleteMailProperties(long chatId, String login, String email);

    List<ActiveChat> getActiveChats();
}
