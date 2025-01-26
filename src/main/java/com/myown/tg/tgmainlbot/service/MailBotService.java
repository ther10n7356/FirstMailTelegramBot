package com.myown.tg.tgmainlbot.service;

import com.myown.tg.tgmainlbot.config.BotCommand;
import com.myown.tg.tgmainlbot.config.BotConfig;
import com.myown.tg.tgmainlbot.controller.MailController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MailBotService extends TelegramLongPollingBot {
    private Logger log = Logger.getLogger(this.getClass().getName());
    private final BotConfig botConfig;
    private final MailController mailController;
    private String lastCommand;

    @Autowired
    public MailBotService(BotConfig botConfig, MailController mailController) {
        super(botConfig.getApiKey());
        this.botConfig = botConfig;
        this.mailController = mailController;
        lastCommand = new String();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String login = message.getFrom().getUserName();
        String messageText = message.getText();
        log.info(String.format("User %s from chat_id %s send command", login, chatId));
        if (BotCommand.START.getCommand().equals(messageText)) {
            sendMessage(chatId, "Привет, " + message.getFrom().getLastName() + "Добро пожаловать, в тестовый почтовый проект.");
            mailController.addNewUser(login, chatId, message.getFrom().getLastName() + " " + message.getFrom().getFirstName());
            log.info("Got start command");
        } else if (BotCommand.ADD_PROPERTIES.getCommand().equals(messageText)) {
            sendMessage(
                    chatId,
                    "Добавление настроек: \n " +
                            "Настройки вводятся одним сообщением. Пример: \n" +
                            "\n" +
                            "test.ru\n" +
                            "name@test.ru\n" +
                            "пароль"
            );
            lastCommand = messageText;
            log.info("Got new mail properties");
        } else if (BotCommand.GET_PROPERTIES.getCommand().equals(messageText)) {
            sendMessage(chatId, "Текущие настройки:");
            sendMessage(chatId, mailController.getEmailProperties(login).toString());
            log.info("Show user e-mail properties");
        } else if (messageText.startsWith(BotCommand.DEL_PROPERTIES.getCommand())) {
            log.info("Gor command for delete properties");
            sendMessage(chatId,"Удаление настроек..");
            String email = messageText.substring(BotCommand.DEL_PROPERTIES.getCommand().length()).trim();
            if (email.isEmpty()) {
                sendMessage(chatId, "Не указаны настройки для удаления. \n" +
                        "Настройки для удаления вводится после комманды.\n " +
                        "Пример: \"/delete_props test@test.ru\"");
            } else {
                log.info("Get command delete properties: " + email);
                try {
                    mailController.deleteMailProperties(chatId, login, email);
                } catch (BadSqlGrammarException e) {
                    sendMessage(chatId, "Переданы неизвестные настройки! Проверьте правильность введнных данных.");
                }
            }
        } else if (BotCommand.DISABLE_NOTIFY.getCommand().equals(messageText)) {
            log.info("Disable properties");
            mailController.deleteActiveChat(chatId, login);
            sendMessage(chatId, "Уведомления отключены. Для повторного включения уведомлений введите команду \"/enable\"");
        } else if (BotCommand.ENABLE_NOTIFY.getCommand().equals(messageText)) {
            log.info("Turn on mail notifications");
            mailController.addActiveChat(chatId, login);
            sendMessage(chatId, "Уведомления включены. Для отключения введите команду \"/disable\"");
        } else if (!lastCommand.isEmpty()) {
            String[] props = message.getText().split("\n");
            mailController.setMailProperties(login, props[0], props[1], props[2]);
            sendMessage(chatId, "Настройки добавлены");
        } else {
            sendMessage(chatId, "команда не найдена");
            log.info(String.format("Undefined command: %s", messageText));
        }
}

    public void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Возникла ошибка", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }
}
