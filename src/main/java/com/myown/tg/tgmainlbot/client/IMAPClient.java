package com.myown.tg.tgmainlbot.client;

import com.myown.tg.tgmainlbot.model.EmailProperties;
import jakarta.mail.*;

import java.io.Closeable;
import java.util.Properties;

public class IMAPClient implements Closeable {
    private final EmailProperties properties;
    private static Properties emailProperties;
    private Folder currentFolder;
    private Store store;

    public IMAPClient(EmailProperties properties) {
        this.properties = properties;
        String imapHost = "imap." + properties.getHost().trim();
        emailProperties = System.getProperties();
        emailProperties.setProperty("mail.transport.protocol","imap");
        emailProperties.put("mail.imap.host", imapHost);
        emailProperties.put("mail.imap.port", "993");
        emailProperties.put("mail.imap.ssl.enable", "true");
        emailProperties.put("mail.imap.auth", "true");
        emailProperties.put("mail.debug", "false");
        connect();
    }

    public void connect() {
        try {
            Session session = Session.getDefaultInstance(emailProperties, null);
            store = session.getStore("imap");
            store.connect(properties.getUsername(), properties.getPassword());
            currentFolder= store.getFolder("INBOX");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Folder getFolder() {
        return currentFolder;
    }

    public Folder[] getFolders() throws MessagingException {
        return currentFolder.list();
    }

    @Override
    public void close() {
        try {
            store.close();
        } catch (MessagingException e) {
            System.out.println("Already closed");
            System.out.println(e.getMessage());
        }
    }
}
