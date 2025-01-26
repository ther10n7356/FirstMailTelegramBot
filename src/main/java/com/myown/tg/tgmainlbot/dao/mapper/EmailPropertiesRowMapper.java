package com.myown.tg.tgmainlbot.dao.mapper;

import com.myown.tg.tgmainlbot.model.EmailProperties;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailPropertiesRowMapper implements RowMapper<EmailProperties> {
    @Override
    public EmailProperties mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EmailProperties(
                rs.getString("tg_mail_host"),
                rs.getString("tg_email"),
                rs.getString("tg_email_password")
        );
    }
}
