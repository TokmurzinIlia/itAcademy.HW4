package dbMetod;

import java.sql.*;

public class DBMethod {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/";
    static final String USER = "postgres";
    static final String PASS = "ASD123asd";

    private static Connection con = null;
    private static PreparedStatement stmt = null;
    private static ResultSet rs = null;

    public static Connection connectToDB(String dbName) {


        try {
            Class.forName("org.postgresql.Driver");

            String url = DB_URL + dbName.toLowerCase();

            con = DriverManager.getConnection(url, USER, PASS);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return con;
    }

    public static void closeConnection() {

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void createInquiry(String dbName, String query) {
        try {
            stmt = connectToDB(dbName).prepareStatement(query);

            stmt.executeUpdate();

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void dropTable(String dbName, String tableName) {
        String query = "DROP TABLE " + tableName;
        try {
            stmt = connectToDB(dbName).prepareStatement(query);

            stmt.executeUpdate();

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}
