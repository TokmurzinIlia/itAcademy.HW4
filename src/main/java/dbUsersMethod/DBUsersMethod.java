package dbUsersMethod;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;

public class DBUsersMethod {

    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/users";
    static final String USER = "postgres";
    static final String PASS = "ASD123asd";

    private static Connection con = null;
    private static PreparedStatement stmt = null;
    private static ResultSet rs = null;

    public static String tableUsers = "CREATE TABLE Users"
            + "("
            + "userId INTEGER NOT NULL UNIQUE,"
            + "name VARCHAR(50) NOT NULL,"
            + "address VARCHAR(255) NULL,"
            + "PRIMARY KEY(userId)"
            + ")";

    public static String tableAccounts = "CREATE TABLE Accounts"
            + "("
            + "accountId INTEGER NOT NULL UNIQUE,"
            + "userId INTEGER NOT NULL,"
            + "balance DECIMAL(10, 3) NOT NULL CHECK(balance <= 2000000000 AND balance >= 0),"
            + "currency VARCHAR(10) NULL UNIQUE,"
            + "PRIMARY KEY(accountId),"
            + "FOREIGN KEY(userId) REFERENCES users (userId)"
            + ")";

    public static String tableTransactions = "CREATE TABLE Transactions"
            + "("
            + "transactinId INTEGER NOT NULL UNIQUE,"
            + "accountId INTEGER NOT NULL,"
            + "amount DECIMAL(10, 3) NOT NULL CHECK(amount <= 100000000),"
            + "PRIMARY KEY(transactinId),"
            + "FOREIGN KEY(accountId) REFERENCES accounts (accountId)"
            + ")";


    public static Connection connectToDBUsers() {


        try {
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(DB_URL, USER, PASS);
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


    public static void addUser(int userID, String name, String address) {

        String query = "INSERT INTO USERS (userId, name, address) VALUES (?, ?, ?)";
        try {

            stmt = connectToDBUsers().prepareStatement(query);

            stmt.setInt(1, userID);
            stmt.setString(2, name);
            stmt.setString(3, address);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
        public static void addUserExcludeAdress(int userID, String name){

        String query = "INSERT INTO USERS (userId, name) VALUES (?, ?)";
        try {

            stmt = connectToDBUsers().prepareStatement(query);

            stmt.setInt(1, userID);
            stmt.setString(2, name);

            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    public static void addAccountUser(int userID, int accountId, double balance, String currency) {

        String query = "INSERT INTO ACCOUNTS (userID, accountId, balance, currency) VALUES (?, ?, ?, ?)";
        try {

            stmt = connectToDBUsers().prepareStatement(query);

            stmt.setInt(1, userID);
            stmt.setInt(2, accountId);
            stmt.setBigDecimal(3, BigDecimal.valueOf(balance));
            stmt.setString(4, currency);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void addAccountUserExcludeCurrency(int userID, int accountId, double balance) {

        String query = "INSERT INTO ACCOUNTS (userID, accountId, balance) VALUES (?, ?, ?)";
        try {

            stmt = connectToDBUsers().prepareStatement(query);

            stmt.setInt(1, userID);
            stmt.setInt(2, accountId);
            stmt.setBigDecimal(3, BigDecimal.valueOf(balance));


            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void topUpBalance (int userID, String currency, double valueTransaction){

        String querySelect = "SELECT * FROM accounts " +
                "WHERE accounts.userid=? AND accounts.currency~*?";

        String queryUpdateAccountsBalance = "UPDATE accounts " +
                "SET balance=? " +
                "WHERE accounts.userid=? AND accounts.currency~*?";

        String queryInsertTransaction = "INSERT INTO TRANSACTIONS (transactinId, accountId, amount) VALUES (?, ?, ?)";

        int accountId = 0;
        double balance = 0;

        try {

            stmt = connectToDBUsers().prepareStatement(querySelect);

            try {
                con.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            stmt.setInt(1, userID);
            stmt.setString(2, currency);

            rs = stmt.executeQuery();

            while (rs.next()) {
                accountId = rs.getInt(1);
                balance = Double.parseDouble(String.valueOf(rs.getBigDecimal(3)));
            }

            balance +=  Math.abs(valueTransaction);

//            System.out.println(balance);
//            System.out.println(accountId);

            stmt = con.prepareStatement(queryUpdateAccountsBalance);

            stmt.setBigDecimal(1, BigDecimal.valueOf(balance));
            stmt.setInt(2, userID);
            stmt.setString(3, currency);

            stmt.executeUpdate();

            stmt = con.prepareStatement(queryInsertTransaction);

            stmt.setInt(1, (int) (Math.random() * 100000));
            stmt.setInt(2, accountId);
            stmt.setBigDecimal(3, BigDecimal.valueOf(valueTransaction));

            stmt.executeUpdate();

            con.commit();


        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
//            System.out.println(balance);
//            System.out.println(accountId);
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection();
        }

    }


    public static void withdrawalOfFundsBalance (int userID, String currency, double valueTransaction){

        String querySelect = "SELECT * FROM accounts " +
                "WHERE accounts.userid=? AND accounts.currency~*?";

        String queryUpdateAccountsBalance = "UPDATE accounts " +
                "SET balance=? " +
                "WHERE accounts.userid=? AND accounts.currency~*?";

        String queryInsertTransaction = "INSERT INTO TRANSACTIONS (transactinId, accountId, amount) VALUES (?, ?, ?)";

        int accountId = 0;
        double balance = 0;

        try {

            stmt = connectToDBUsers().prepareStatement(querySelect);

            try {
                con.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            stmt.setInt(1, userID);
            stmt.setString(2, currency);

            rs = stmt.executeQuery();

            while (rs.next()) {
                accountId = rs.getInt(1);
                balance = Double.parseDouble(String.valueOf(rs.getBigDecimal(3)));
            }

            balance -=  Math.abs(valueTransaction);

            System.out.println(balance);
//            System.out.println(accountId);

            stmt = con.prepareStatement(queryUpdateAccountsBalance);

            stmt.setBigDecimal(1, BigDecimal.valueOf(balance));
            stmt.setInt(2, userID);
            stmt.setString(3, currency);

            stmt.executeUpdate();

            stmt = con.prepareStatement(queryInsertTransaction);

            stmt.setInt(1, (int) (Math.random() * 100000));
            stmt.setInt(2, accountId);
            stmt.setBigDecimal(3, BigDecimal.valueOf(valueTransaction*(-1)));

            stmt.executeUpdate();

            con.commit();


        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
//            System.out.println(balance);
//            System.out.println(accountId);
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            closeConnection();}
        }

}
