import dbMetod.DBCreator;
import dbMetod.DBMethod;


import static dbUsersMethod.DBUsersMethod.*;

public class Main {
    public static void main(String[] args) {


        DBCreator.createDB("Users");
        DBCreator.checkAvailabilityDB("Users");
        DBMethod.createInquiry("Users", tableUsers);
        DBMethod.createInquiry("Users", tableAccounts);
        DBMethod.createInquiry("Users", tableTransactions);

        //DBCreator.dropDB("Users");

    }
}
