package dbUsersMethod;

import dbMetod.DBCreator;

import java.math.BigDecimal;
import java.sql.*;


public class DBUsersMethod {


    private static Connection con = null;
    private static PreparedStatement stmt = null;
    private static ResultSet rs = null;

    public static String tableUsers = "CREATE TABLE Users"
            + "("
            + "userId INTEGER NOT NULL UNIQUE,"
            + "name VARCHAR(50) NOT NULL UNIQUE,"
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

            con = DriverManager.getConnection(DBCreator.PG_URL + "users", DBCreator.USER, DBCreator.PASS);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return con;
    }

    public static void closeConnection() {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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

    public static void addAccountUser(int userID, double balance, String currency) {

        String query = "INSERT INTO ACCOUNTS (userID, accountId, balance, currency) VALUES (?, ?, ?, ?)";
        try {

            stmt = connectToDBUsers().prepareStatement(query);

            stmt.setInt(1, userID);
            stmt.setInt(2, (int)(Math.random()*1000000));
            stmt.setBigDecimal(3, BigDecimal.valueOf(balance));
            stmt.setString(4, currency);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void addAccountUserExcludeCurrency(int userID, double balance) {

        String query = "INSERT INTO ACCOUNTS (userID, accountId, balance) VALUES (?, ?, ?)";
        try {

            stmt = connectToDBUsers().prepareStatement(query);

            stmt.setInt(1, userID);
            stmt.setInt(2, (int)(Math.random()*1000000));
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

        String querySelectBalance = "SELECT * FROM accounts " +
                "WHERE accounts.userid=? AND accounts.currency~*?";

        String queryUpdateAccountsBalance = "UPDATE accounts " +
                "SET balance=? " +
                "WHERE accounts.userid=? AND accounts.currency~*?";

        String queryInsertTransaction = "INSERT INTO TRANSACTIONS (transactinId, accountId, amount) VALUES (?, ?, ?)";

        int accountId = 0;
        double balance = 0;

        try {

            stmt = connectToDBUsers().prepareStatement(querySelect);


            con.setAutoCommit(false);

            stmt.setInt(1, userID);
            stmt.setString(2, currency);

            rs = stmt.executeQuery();

            while (rs.next()) {
                accountId = rs.getInt(1);
                balance = Double.parseDouble(String.valueOf(rs.getBigDecimal(3)));
            }

            balance +=  Math.abs(valueTransaction);

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

            System.out.println("Transaction completed");


        } catch (SQLException e) {
            try {
                con.rollback();
                System.out.println("Transaction declined");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeConnection();
        }


        try {

            stmt = connectToDBUsers().prepareStatement(querySelectBalance);

            stmt.setInt(1, userID);
            stmt.setString(2, currency);

            rs = stmt.executeQuery();

            while (rs.next()) {
                accountId = rs.getInt(1);
                balance = Double.parseDouble(String.valueOf(rs.getBigDecimal(3)));
            }

            System.out.println("Account Id: " + accountId);
            System.out.println("Actual balance: " + balance + " " + currency);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            closeConnection();
        }
    }


        public static void withdrawalOfFundsBalance (int userID, String currency, double valueTransaction){

        String querySelect = "SELECT * FROM accounts " +
                "WHERE accounts.userid=? AND accounts.currency~*?";

        String querySelectBalance = "SELECT * FROM accounts " +
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

            System.out.println("Transaction completed");

        } catch (SQLException e) {
            try {
                con.rollback();
                System.out.println("Transaction declined");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            //e.printStackTrace();
        } finally {

            closeConnection();
                }

        try {

                stmt = connectToDBUsers().prepareStatement(querySelectBalance);

                stmt.setInt(1, userID);
                stmt.setString(2, currency);

                rs = stmt.executeQuery();

                while (rs.next()) {
                    accountId = rs.getInt(1);
                    balance = Double.parseDouble(String.valueOf(rs.getBigDecimal(3)));
                }

                System.out.println("Account Id: " + accountId);
                System.out.println("Actual balance: " + balance + " " + currency);
        } catch (SQLException throwables) {
                throwables.printStackTrace();
        } finally {
                closeConnection();
            }
        }

}
