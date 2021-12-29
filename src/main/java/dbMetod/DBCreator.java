package dbMetod;

import java.sql.*;

public class DBCreator {

    public static final String PG_URL = "jdbc:postgresql://127.0.0.1:5432/";
    public static final String USER = "postgres";
    public static final String PASS = "ASD123asd";

    private static Connection con = null;
    private static PreparedStatement stmt = null;

    public static Connection connectToPG() {


        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(PG_URL, USER, PASS);
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

    public static void createDB(String dbName) {

        String query = "CREATE DATABASE " + dbName;

        try {
            stmt = connectToPG().prepareStatement(query);

            stmt.executeUpdate();

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void dropDB(String dbName) {

        String query = "DROP DATABASE " + dbName;

        try {
            stmt = connectToPG().prepareStatement(query);

            stmt.executeUpdate();

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void checkAvailabilityDB(String dbName) {


        try {
            Class.forName("org.postgresql.Driver");
            String dbUrl = "jdbc:postgresql://127.0.0.1:5432/" + dbName.toLowerCase();
            con = DriverManager.getConnection(dbUrl, USER, PASS);
            System.out.println("DB is exist");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("DB is not exist");
        } finally {
            closeConnection();
        }

    }
    public static Connection connectToDB(String dbName) {


        try {
            Class.forName("org.postgresql.Driver");

            String url = DBCreator.PG_URL + dbName.toLowerCase();

            con = DriverManager.getConnection(url, DBCreator.USER, DBCreator.PASS);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return con;
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

