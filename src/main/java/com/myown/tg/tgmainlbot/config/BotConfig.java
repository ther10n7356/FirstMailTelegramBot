package com.myown.tg.tgmainlbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class BotConfig {
    @Value("${bot.name}")
    private String name;

    @Value("${bot.api.key}")
    private String apiKey;

    public String getName() {
        return name;
    }

    public String getApiKey() {
        return apiKey;
    }
}
