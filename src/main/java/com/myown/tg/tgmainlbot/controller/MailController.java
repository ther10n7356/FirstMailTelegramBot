package com.myown.tg.tgmainlbot.controller;

import com.myown.tg.tgmainlbot.dao.TgmailDao;
import com.myown.tg.tgmainlbot.model.ActiveChat;
import com.myown.tg.tgmainlbot.model.EmailProperties;
import com.myown.tg.tgmainlbot.model.ImapMessage;
import com.myown.tg.tgmainlbot.service.ImapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class MailController {
    private Logger log = Logger.getLogger(this.getClass().getName());
    private final ImapService imapService;
    private final TgmailDao tgmailDao;

    @Autowired
    public MailController(ImapService imapService, TgmailDao tgmailDao) {
        this.imapService = imapService;
        this.tgmailDao = tgmailDao;
    }

    public void printMessage(EmailProperties properties) {
        for (ImapMessage message: imapService.getUnreadMessages(properties)) {
            log.info(getStringMessage(message));
        }
    }

    public void addNewUser(String login, long chatId, String username) {
        tgmailDao.addNewUser(login, chatId, username);
    }

    public List<String> getNewMessages(EmailProperties properties) {
        List<String> results = new ArrayList<>();
        for (ImapMessage message: imapService.getUnreadMessages(properties)) {
            results.add(getStringMessage(message));
        }

        return results;
    }

    public void addActiveChat(long chatId, String login) {
        tgmailDao.addActiveChat(chatId, login);
    }

    public void setMailProperties(String username, String host, String email, String password) {
        tgmailDao.addMailProperties(username, host, email, password);
    }

    public List<ActiveChat> getActiveChats() {
        return tgmailDao.getActiveChats();
    }

    public List<EmailProperties>  getEmailProperties(String login) {
        return tgmailDao.getMailPropertiesByUser(login);
    }

    public void deleteActiveChat(long chatId, String login) {
        tgmailDao.deleteActiveChat(chatId, login);
    }

    public void deleteMailProperties(long chatId, String login, String email) {
        tgmailDao.deleteMailProperties(chatId, login, email);
    }

    private String getStringMessage(ImapMessage message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Folder: ").append(message.getFolderName()).append("\n");
        sb.append("Message from: ").append(message.getMailFrom().toString()).append("\n");
        sb.append("Sent: ").append(message.getSentDate()).append("\n");
        sb.append("Subject: ").append(message.getSubject()).append("\n");
        sb.append("Content: ").append(message.getMailContent()).append("\n");

        return sb.toString();
    }
}
