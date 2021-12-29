
import dbUsersMethod.DBUsersMethod;

import java.sql.SQLException;


public class Main2 {
    public static void main(String[] args) {
//        DBUsersMethod.addUserExcludeAdress(1, "Ilia");
//        DBUsersMethod.addAccountUser(1, 20000.00, "EURO");
//        DBUsersMethod.addAccountUser(1, 1000.00, "USD");
        DBUsersMethod.topUpBalance(1,"usd", 1000000);
        DBUsersMethod.withdrawalOfFundsBalance(1,"usd", 100000000);


    }
}
