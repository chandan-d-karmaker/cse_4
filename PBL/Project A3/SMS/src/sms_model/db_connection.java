package sms_model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** One place for the database settings. Change them here only. */
public class db_connection {
    private static final String URL  = "jdbc:mysql://localhost:3306/sms_a";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
