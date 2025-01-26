package com.myown.tg.tgmainlbot.service;

import com.myown.tg.tgmainlbot.client.IMAPClient;
import com.myown.tg.tgmainlbot.model.EmailProperties;
import com.myown.tg.tgmainlbot.model.ImapMessage;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImapService {

    private final List<ImapMessage> unreadMessages;

    @Autowired
    public ImapService() {
        this.unreadMessages = new ArrayList<>();
    }

    public List<ImapMessage> getUnreadMessages(EmailProperties properties) {
        unreadMessages.clear();
        try (IMAPClient imapClient = new IMAPClient(properties)) {
            System.out.println(imapClient.getFolder().getFullName());
            findUnreadMessage(imapClient.getFolder());

            for (Folder folder : imapClient.getFolders()) {
                System.out.println(folder.getFullName());
                findUnreadMessage(folder);
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }

        return unreadMessages;
    }

    private void findUnreadMessage(Folder currentFolder) throws MessagingException, IOException {
        currentFolder.open(Folder.READ_ONLY);
        for (Message message : currentFolder.getMessages()) {
            if (!message.isSet(Flags.Flag.SEEN)
                    && message.getSentDate().after(getCheckDate())) {
                String fromAddress = Arrays.stream(message.getFrom())
                        .map(a -> ((InternetAddress)a).getAddress())
                        .collect(Collectors.joining());
                Date sentDate = message.getSentDate();
                String content = getTextFromMessage(message);
                String subject = message.getSubject();
                unreadMessages.add(
                        new ImapMessage(
                                subject,
                                content,
                                sentDate,
                                fromAddress,
                                currentFolder.getFullName()
                        )
                );
            }
        }
    }

    private String getTextFromMessage(Message message) throws IOException, MessagingException {
        if (message.isMimeType("text/plain"))
        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            return getTextFromMimeMultipart(mimeMultipart);
        }
        return "";
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                return result + "\n" + bodyPart.getContent();
            }

            result += this.parseBodyPart(bodyPart);
        }
        return result;
    }

    private String parseBodyPart(BodyPart bodyPart) throws IOException, MessagingException {
        if (bodyPart.isMimeType("text/plain")) {
            return "\n" + Jsoup.parse(bodyPart.getContent().toString()).text();
        }

        if (bodyPart.getContent() instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }

        return "";
    }

    private LocalDateTime getLocalDateTime() {
        return LocalDateTime.now().minusMinutes(60L);
    }

    private Date getCheckDate() {
        return Date.from(getLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
    }
}
