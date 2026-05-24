package service;


import db.DatabaseManager;
import util.ErrorHandler;
import util.LogHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffHandler implements IStaffHandler {

    public boolean verify(String username, String password) {
        String query = "SELECT password FROM staff WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password").equals(password);
                }
            }
        } catch (SQLException e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "DB Error", "Login check failed.", false);
        }
        return false;
    }



    }
