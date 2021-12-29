package main;

import dbMetod.DBCreator;
import dbUsersMethod.DBUsersMethod;


import static dbUsersMethod.DBUsersMethod.*;

public class Main {
    public static void main(String[] args) {




        DBCreator.createDB("Users");
        DBCreator.checkAvailabilityDB("Users");
        DBCreator.createInquiry("Users", tableUsers);
        DBCreator.createInquiry("Users", tableAccounts);
        DBCreator.createInquiry("Users", tableTransactions);
        DBUsersMethod.addUserExcludeAdress(1, "Ilia");
        DBUsersMethod.addAccountUser(1, 20000.00, "EURO");
        DBUsersMethod.addAccountUser(1, 1000.00, "USD");
        DBUsersMethod.topUpBalance(1,"usd", 1000000);
        DBUsersMethod.withdrawalOfFundsBalance(1,"usd", 100000000);

        DBCreator.dropDB("Users");

    }
}
