package com.myown.tg.tgmainlbot.dao.mapper;

import com.myown.tg.tgmainlbot.model.ActiveChat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActiveChatRowMapper implements RowMapper<ActiveChat> {
    @Override
    public ActiveChat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ActiveChat(
                rs.getLong("tg_chat_id"),
                rs.getString("tg_user")
        );
    }
}
