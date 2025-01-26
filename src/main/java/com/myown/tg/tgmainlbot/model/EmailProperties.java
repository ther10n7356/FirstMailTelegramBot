package com.myown.tg.tgmainlbot.model;

public class EmailProperties {
    private final String host;
    private final String username;
    private final String password;

    public EmailProperties(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Mail properties: \n" +
                "host='" + host + "\\'\n" +
                "username='" + username + "\\'\n" +
                "password=***";
    }
}
