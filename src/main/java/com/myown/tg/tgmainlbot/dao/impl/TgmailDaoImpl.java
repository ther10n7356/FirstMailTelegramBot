package com.myown.tg.tgmainlbot.dao.impl;

import com.myown.tg.tgmainlbot.dao.TgmailDao;
import com.myown.tg.tgmainlbot.dao.mapper.ActiveChatRowMapper;
import com.myown.tg.tgmainlbot.dao.mapper.EmailPropertiesRowMapper;
import com.myown.tg.tgmainlbot.model.ActiveChat;
import com.myown.tg.tgmainlbot.model.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TgmailDaoImpl implements TgmailDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TgmailDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void addNewUser(String login, long chatId, String username) {
        jdbcTemplate.execute(
                "call add_new_tg_user(:login, :chat_id, :username)",
                Map.of(
                        "login", login,
                        "chat_id", chatId,
                        "username", username
                ),
                PreparedStatement::execute
        );
    }

    @Override
    public void addMailProperties(String login, String host, String email, String password) {
        jdbcTemplate.execute(
                "call add_tg_mail_properties(:login, :host, :email, :password)",
                Map.of(
                        "login", login,
                        "host", host,
                        "email", email,
                        "password", password
                ),
                PreparedStatement::execute
        );
    }

    @Override
    public List<EmailProperties> getMailPropertiesByUser(String login) {
        return jdbcTemplate.query(
                "select\n" +
                        "\ttg_mail_host,\n" +
                        "\ttg_email,\n" +
                        "\ttg_email_password\n" +
                        "from\n" +
                        "\ttgmail_properties tp\n" +
                        "join tgmail_users tu on\n" +
                        "\ttp.tg_user_id = tu.tg_user_id\n" +
                        "where\n" +
                        "\ttu.tg_user = :login",
                Map.of("login", login),
                new EmailPropertiesRowMapper()
        );
    }

    @Override
    public void addActiveChat(long chatId, String login) {
        jdbcTemplate.execute(
                "call add_active_chat(:chat_id, :login)",
                Map.of(
                        "chat_id", chatId,
                        "login", login
                ),
                PreparedStatement::execute
                );
    }

    @Override
    public void deleteActiveChat(long chatId, String login) {
        jdbcTemplate.update(
                "delete\n" +
                        "from\n" +
                        "\ttgmail_active_chat tac\n" +
                        "where\n" +
                        "\tac_user_id in (\n" +
                        "\tselect\n" +
                        "\t\ttg_user_id\n" +
                        "\tfrom\n" +
                        "\t\ttgmail_users tu\n" +
                        "\twhere\n" +
                        "\t\ttg_user = :username\n" +
                        "\tand tg_chat_id = :chat_id)",
                Map.of(
                        "username", login,
                        "chat_id", chatId
                )
        );
    }

    @Override
    public void deleteMailProperties(long chatId, String login, String email) {
        jdbcTemplate.update(
                "delete \n" +
                        "from\n" +
                        "\ttgmail_properties tac\n" +
                        "where\n" +
                        "and tg_email = :email\n" +
                        "\ttg_user_id in (\n" +
                        "\tselect\n" +
                        "\t\ttg_user_id\n" +
                        "\tfrom\n" +
                        "\t\ttgmail_users tu\n" +
                        "\twhere\n" +
                        "\t\ttg_user = :username\n" +
                        "\tand tg_chat_id = :chat_id)",
                Map.of(
                        "username", login,
                        "chat_id", chatId,
                        "email", email
                )
        );
    }

    @Override
    public List<ActiveChat> getActiveChats() {
        return jdbcTemplate.query(
                "select tg_chat_id\n" +
                        "     , tg_user \n" +
                        "  from tgmail_active_chat tac\n" +
                        "  join tgmail_users tu on tu.tg_user_id = tac.ac_user_id;",
                new HashMap<>(0),
                new ActiveChatRowMapper()
        );
    }
}
