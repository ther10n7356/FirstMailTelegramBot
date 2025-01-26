package com.myown.tg.tgmainlbot.model;

import jakarta.mail.Address;

import java.util.Date;

public class ImapMessage {

    private final String subject;
    private final String mailContent;
    private final Date sentDate;
    private final String mailFrom;
    private final String folderName;

    public ImapMessage(String subject, String mailContent, Date sentDate, String mailFrom, String folderName) {
        this.subject = subject;
        this.mailContent = mailContent;
        this.sentDate = sentDate;
        this.mailFrom = mailFrom;
        this.folderName = folderName;
    }

    public String getSubject() {
        return subject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getFolderName() {
        return folderName;
    }

    @Override
    public String toString() {
        return "ImapMessage{" +
                "subject='" + subject + '\'' +
                ", mailContent='" + mailContent + '\'' +
                ", sentDate=" + sentDate +
                ", mailFrom=" + mailFrom +
                '}';
    }
}
