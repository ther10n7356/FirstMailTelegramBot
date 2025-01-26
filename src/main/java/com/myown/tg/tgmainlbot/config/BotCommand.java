package com.myown.tg.tgmainlbot.config;

import java.util.HashMap;
import java.util.Map;

public enum BotCommand {
    START("/start"),
    ADD_PROPERTIES("/add_props"),
    GET_PROPERTIES("/show_props"),
    DEL_PROPERTIES("/delete_props"),
    DISABLE_NOTIFY("/disable"),
    ENABLE_NOTIFY("/enable");

    private final String command;

    BotCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
