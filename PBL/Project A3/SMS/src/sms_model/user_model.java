package sms_model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** All SQL for the users table. */
public class user_model {

    public void insert_user(String username, String pass, String role) throws SQLException {
        String sql = "INSERT INTO users(username,password,role) VALUES(?,?,?)";
        try (Connection con = db_connection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, pass);
            ps.setString(3, role);
            ps.executeUpdate();
        }
    }

    /** Returns {id, username, role} or null if the login is wrong. */
    public Object[] find_user(String username, String pass) throws SQLException {
        String sql = "SELECT id,username,role FROM users WHERE username=? AND password=?";
        try (Connection con = db_connection.get();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)};
                }
            }
        }
        return null;
    }

    /** Rows of {id, username, role}. */
    public List<Object[]> get_all_users() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        try (Connection con = db_connection.get();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id,username,role FROM users ORDER BY id")) {
            while (rs.next()) {
                list.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3)});
            }
        }
        return list;
    }

    public void delete_user(int id) throws SQLException {
        try (Connection con = db_connection.get();
             PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
