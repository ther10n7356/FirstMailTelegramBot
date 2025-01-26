package com.myown.tg.tgmainlbot.service;

import com.myown.tg.tgmainlbot.controller.MailController;
import com.myown.tg.tgmainlbot.model.ActiveChat;
import com.myown.tg.tgmainlbot.model.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ScheduleService {
    private Logger log = Logger.getLogger(this.getClass().getName());
    private final MailController controller;
    private final MailBotService botService;

    @Autowired
    public ScheduleService(MailController controller, MailBotService botService) {
        this.controller = controller;
        this.botService = botService;
    }

    @Scheduled(cron = "0 */5  * ? * *")
    public void notifyAboutNewMessages() {
        List<ActiveChat> chats = controller.getActiveChats();
        for (ActiveChat activeChat: chats) {
            log.info("Active chat " + activeChat.getChatId() + ", username: " + activeChat.getTgLogin() );
            try {
                List<EmailProperties> properties = controller.getEmailProperties(activeChat.getTgLogin());
                for (EmailProperties emailProperties: properties) {
                    List<String> messages = controller.getNewMessages(emailProperties);
                    for (String message : messages) {
                        botService.sendMessage(activeChat.getChatId(), message);
                    }
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "произошла ошибка", e);
            }

        }
    }
}
