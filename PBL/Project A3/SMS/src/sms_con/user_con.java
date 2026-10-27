package sms_con;

import java.sql.SQLException;
import java.util.List;
import sms_model.user_model;

public class user_con extends user_model {

    /** Checks the login and, if correct, stores it in session. */
    public boolean login(String username, String pass) throws SQLException {
        Object[] u = find_user(username, pass);
        if (u == null) return false;
        session.user_id = (Integer) u[0];
        session.username = (String) u[1];
        session.role = (String) u[2];
        return true;
    }

    public void set_user(String username, String pass, String role) throws SQLException {
        if (username.isBlank() || pass.isBlank()) {
            throw new IllegalArgumentException("Username and password are required.");
        }
        insert_user(username.trim(), pass, role);
    }

    public List<Object[]> get_users() throws SQLException {
        return get_all_users();
    }

    public void remove_user(int id) throws SQLException {
        if (id == session.user_id) {
            throw new IllegalArgumentException("You cannot delete the account you are logged in with.");
        }
        delete_user(id);
    }
}
