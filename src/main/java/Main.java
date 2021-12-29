import dbMetod.DBCreator;


import static dbUsersMethod.DBUsersMethod.*;

public class Main {
    public static void main(String[] args) {




        DBCreator.createDB("Users");
        DBCreator.checkAvailabilityDB("Users");
        DBCreator.createInquiry("Users", tableUsers);
        DBCreator.createInquiry("Users", tableAccounts);
        DBCreator.createInquiry("Users", tableTransactions);

//        DBCreator.dropDB("Users");

    }
}
