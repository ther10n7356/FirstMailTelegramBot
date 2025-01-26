package com.myown.tg.tgmainlbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class TgmailbotApplication {

	public static void main(String[] args) throws MessagingException, IOException {
		SpringApplication.run(TgmailbotApplication.class, args);
	}

}
