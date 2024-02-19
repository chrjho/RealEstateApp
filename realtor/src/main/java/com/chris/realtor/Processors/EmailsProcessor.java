package com.chris.realtor.Processors;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.chris.realtor.Models.EmailDB;
import com.chris.realtor.Models.House;
import com.chris.realtor.Models.SellRequest;

@Repository
public class EmailsProcessor {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmailsProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertEmail(EmailDB emailDB){
        String sql = "{call InsertEmail(?,?,?,?) }";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1, emailDB.getSender());
            cs.setString(2, emailDB.getRecipient());
            cs.setString(3, emailDB.getMessage());
            cs.setString(4, emailDB.getListing());

            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<EmailDB> getEmails(String email){
        String sql = "{call GetEmails(?) }";
        ArrayList<EmailDB> emails = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, email);
    
            if (cs.execute()) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) {
                        String sender = rs.getString("sender");
                        String recipient = rs.getString("recipient");
                        String message = rs.getString("message");
                        String timestamp = rs.getString("timestamp");
                        String listing = rs.getString("listing");
                        String thread = rs.getString("thread");

                        EmailDB emailDB = new EmailDB(sender,recipient,message,timestamp,listing,thread);
                        emails.add(emailDB);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return emails;
        }
        return emails;
    }
}