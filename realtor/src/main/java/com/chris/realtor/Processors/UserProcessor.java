package com.chris.realtor.Processors;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.chris.realtor.Models.House;
import com.chris.realtor.Models.SellRequest;
import com.chris.realtor.Models.UserRequest;

@Repository
public class UserProcessor {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUser(UserRequest user) {
        String sql = "{call InsertUser(?,?,?) }";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.setString(1, user.getUsername());
            cs.setString(2, user.getEmail());
            cs.setString(3, user.getPassword());

            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyPassword(UserRequest user) {
        String sql = "{ ? = call VerifyUserPassword(?, ?) }";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.INTEGER); // Output parameter for the result
            if(user.getUsername() != null){
                cs.setString(2, user.getUsername());
            }
            else if(user.getEmail() != null){
                cs.setString(2, user.getEmail());
            }
            cs.setString(3, user.getPassword());
            cs.execute();

            int result = cs.getInt(1); // Get the result from the output parameter

            return result == 1; // Return true if password verification succeeded, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUsername(String email){
        String sql = "{call GetUsername(?,?) }";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, email);
            cs.registerOutParameter(2, Types.NVARCHAR);
    
            cs.execute();
            String username = cs.getString(2); // Retrieve the output parameter as a string
            return username;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getEmail(String username){
        String sql = "{call GetEmail(?,?) }";
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, username);
            cs.registerOutParameter(2, Types.NVARCHAR);
    
            cs.execute();
            String email = cs.getString(2); // Retrieve the output parameter as a string
            return email;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }
    
}
