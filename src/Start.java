import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Start {
    int count=0;
    List<String> FileRecords;
    public static final String SET_BOLD_TEXT = "\033[0;1m";
    public void StartBank(){
        @SuppressWarnings("resource")
        Scanner input = new Scanner(System.in);
        char mainYesOrNo = 'Y';
        while (mainYesOrNo =='Y'){
            System.out.print("\t=========================================\n");
            System.out.print(SET_BOLD_TEXT +"\t \tWELCOME TO BANK MANAGEMENT SYSTEM \n");
            if(count<1){
                count=count+1;
            }else{
                System.out.print("\t\t Again \n");
            }
            System.out.print("\t=========================================\n\n");
            if(count<2){
                count=count+1;
            }else{
                System.out.print("\t\t1 : Start Again\n\n");
                System.out.print("\t\t2 : Exit\n\n");
                System.out.println("\tYou Select : ");
            }
            //start the operation
            start();

            System.out.println("\n\tDo u want to run your Program Again \n\t\t\tY = yes\n\t\t\tN = No\n");
            System.out.println("You Select : ");
            mainYesOrNo =(input.next()).charAt(0);
            if(Character.isLowerCase(mainYesOrNo )){
                mainYesOrNo =Character.toUpperCase(mainYesOrNo );
            }
        }
    }

    private void existingUserData(){
        String choice, ch, operation;
        Transaction transac = new Transaction();
        Scanner sc = new Scanner(System.in);
        double amount;
        long accountNo=0;
        long transferAccountNo=0;
        do {
            System.out.print("\tQ: Choose any one of the operation to perform\n\n");
            System.out.print("\t1 : Transaction\n\n");
            System.out.print("\t2 : View Existing Account Information\n\n");
            System.out.print("\t3 : Pay Utility Bills\n\n");
            System.out.print("\tq : Exit\n\n");
            System.out.println("Your choice: ");
            choice = sc.next();
            switch (choice) {
                case "1":
                    System.out.print("\tQ: What do you want to do for Transaction?\n\n");
                    System.out.print("\t\ta : Deposit\n\n");
                    System.out.print("\t\tb : Withdraw\n\n");
                    System.out.print("\t\tc : Transfer\n\n");
                    ch = sc.next();
                    if (ch.equalsIgnoreCase("a"))
                        operation = "Deposit";
                    else if (ch.equalsIgnoreCase("b"))
                        operation = "Withdraw";
                    else if (ch.equalsIgnoreCase("c")) {
                        operation = "Transfer";
                        System.out.println("Please enter Account Number to transfer the amount:");
                        transferAccountNo = sc.nextLong();
                        transac.storeTransferAccount(transferAccountNo);
                    }
                    else {
                        operation = "Invalid option";
                    }
                    System.out.println("Please enter your Account Number:");
                    accountNo = sc.nextLong();
                    System.out.println("Please enter Amount:");
                    amount = sc.nextDouble();
                    transac.transaction("",accountNo, operation, amount);
                    break;
                case "2":
                    System.out.println("Account Number:");
                    accountNo = sc.nextLong();
                    operation = "showInfo";
                    transac.transaction("",accountNo, operation, 0);
                    break;
                case "3":
                    operation = "PayBill";
                    System.out.println("Please enter your Account Number:");
                    accountNo = sc.nextLong();
                    System.out.println("Please enter bill amount:");
                    amount = sc.nextDouble();
                    transac.transaction("",accountNo, operation, amount);
                    break;
                case "q":
                    System.out.println("====== Thank you! ======");
                    return;
                default:
                    error();
            }
        }while (choice != "q");
        sc.close();
    }
    private void start() {
        String choice, ch, operation;
        Transaction transac = new Transaction();
        Scanner sc = new Scanner(System.in);
        double amount;
        long accountNo=0;
        long transferAccountNo=0;
        do {
            System.out.print("\t======== What do you want to do? ========\n\n");
            System.out.print("\t========= 1 : Open New account ==========\n\n");
            System.out.print("\t===== 2 : Do you have existing account? ====\n\n");
            System.out.print("\t============ q : Exit ============\n\n");
            System.out.println("Your choice: ");
            choice = sc.next();
            switch (choice) {
                case "1":
                    double openingBalance;
                    System.out.println("Enter your full name :");
                    sc.nextLine();
                    String name= sc.nextLine();
                    System.out.println("Enter the opening balance :");
                    openingBalance = sc.nextDouble();
                    transac.transaction(name, accountNo, "Opening", openingBalance);
                    accountNo = accountNo+1;
                    break;
                case "2":
                    this.existingUserData();
                case "q":
                    System.out.println("====== Thank you! ======");
                    return;
                default:
                    error();
            }
        } while (choice != "q");
        sc.close();
    }
    public static void error() {
        System.out.print("\t\t *****************************\n");
        System.out.print("\t\t You Select some thing wrong\n");
        System.out.print("\t\t OR\n");
        System.out.print("\t\t There may be some other Problem\n");
        System.out.print("\t\t It is better for you to try again...!\n");
        System.out.print("\t\t *****************************\n\n");
    }
}
