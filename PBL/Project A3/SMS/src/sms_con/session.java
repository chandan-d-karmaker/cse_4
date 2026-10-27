package sms_con;

/** Remembers who is logged in. */
public class session {
    public static int user_id;
    public static String username;
    public static String role;

    public static boolean is_admin() { return "Admin".equals(role); }

    public static void clear() { user_id = 0; username = null; role = null; }
}
