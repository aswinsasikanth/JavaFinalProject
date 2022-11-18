import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;
public class Transaction {
    private String transactionType;
    private String userName;
    private double amount;
    private long accountNum;
    private long transferAccountNum;
    private Date date;
    Scanner sc = new Scanner(System.in);

    public Transaction() {
    }

    public void transaction(String userName, long accountNum, String transactionType, double amount) {
        this.userName = userName;
        this.accountNum = accountNum;
        this.transactionType = transactionType;
        this.amount = amount;
        date = new Date();
        operation();
    }
//findMaxID
    //findDisplay
    public void storeTransferAccount(long transferAccount) {
        this.transferAccountNum = transferAccount;
    }

    private void operation() {
        if (transactionType.equalsIgnoreCase("Opening")) {
            try {
                FileWriter fstream = new FileWriter("MyFile.txt", true);
                BufferedWriter out = new BufferedWriter(fstream);
                int userId = (int) (findMaxId() + 1);
                out.write(Integer.toString(userId) + "\n");
                out.write(amount + "\n");
                out.write(date + "\n");
                out.write(userName + "\n");
                out.close();

                //Account opened
                System.out.println("==== Congratulations! Account has been successfully opened ====");
                System.out.println("\t\tUser Name: " + userName);
                System.out.println("\t\tAccount Number: " + userId);
                System.out.println("\t\tOpening Balance: " + amount + "\n");
            } catch (IOException e) {
                //Catch input output exception
                System.err.println("Caught IOException: " + e.getMessage());
            }
        } else if (transactionType.equalsIgnoreCase("withdraw")) {
            Path path = Paths.get("MyFile.txt");
            //Check whether files exists storing account information
            if (Files.exists(path)) {
                findUpdate();
            } else {
                System.out.println("File not Found");
            }
        } else if (transactionType.equalsIgnoreCase("deposit")) {
            //Check whether files exists storing account information
            Path path = Paths.get("MyFile.txt");
            if (Files.exists(path)) {
                findUpdate();
            } else {
                System.out.println("File not Found");
            }
        } else if (transactionType.equalsIgnoreCase("Paybill")) {
            //Check whether files exists storing account information
            Path path = Paths.get("MyFile.txt");
            if (Files.exists(path)) {
                payBill();
            } else {
                System.out.println("File not Found");
            }
        } else if (transactionType.equalsIgnoreCase("transfer")) {
            //Check whether files exists storing account information
            Path path = Paths.get("MyFile.txt");
            if (Files.exists(path)) {
                transferAmount();
            } else {
                System.out.println("File not Found");
            }
        } else if (transactionType.equalsIgnoreCase("showInfo")) {
            Path path = Paths.get("MyFile.txt");
            if (Files.exists(path)) {
                findDisplay();
            } else {
                System.out.println("File not Found");
            }
        } else {
            System.out.println("Invalid option");
            return;
        }
    }
//To check the account existence ****NOT WORKING AS EXPECTED****
    public double checkIfAccountExists(long accountNum) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("MyFile.txt"));
            String line;
            String accountTmpNum = Long.toString(accountNum);
            int count = 1;
            while ((line = reader.readLine()) != null) {
                if (count > 1) {
                    accountTmpNum = line;
                }
                if ((line.equals(accountTmpNum)) && (count < 5)) {
                    double temp_amount = 0;
                    if (count == 2) {
                        temp_amount = Double.parseDouble(line);
                        return temp_amount;
                    }
                    count = count + 1;
                }
            }
        } catch (Exception e) {
        }
        return 0.0;
    }

    private void payBill() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("MyFile.txt"));
            FileWriter fstream = new FileWriter("TempFile.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            String line;
            String trmpaccountNum = Long.toString(accountNum);
            int count = 1;
            while ((line = reader.readLine()) != null) {
                if (count > 1) {
                    trmpaccountNum = line;
                }
                if ((line.equals(trmpaccountNum)) && (count < 4)) {
                    double temp_amount = 0;
                    if (count == 1) {
                        out.write(accountNum + "\n");
                    } else if (count == 2) {
                        //subtract the bill amount from the balance
                        temp_amount = Double.parseDouble(line) - amount;
                        if (temp_amount < 0) {
                            System.out.println("You do not have sufficient balance \n");
                            out.write(Double.toString(amount) + "\n");
                        } else {
                            System.out.println("\n\t==== Successfully paid bill ====");
                            System.out.println("\t" + Double.toString(temp_amount) + " is your closing balance\n");
                            out.write(Double.toString(temp_amount) + "\n");
                        }
                    } else if (count == 3) {
                        out.write(date + "\n");
                    }
                    count = count + 1;
                } else {
                    out.write(line + "\n");
                }
            }
            out.close();
            reader.close();
            File f1 = new File("MyFile.txt");
            f1.delete();
            File f2 = new File("TempFile.txt");
            boolean b = f2.renameTo(f1);
            if (b) {
            } else {
                System.out.println("Updated has Error");
            }
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", "MyFile.txt");
            e.printStackTrace();
        }
    }

    public void transferAmount() {
        try {
            double transferAccountBalance = this.checkIfAccountExists(transferAccountNum);
            double temp_amount = this.checkIfAccountExists(accountNum);
            if (this.checkIfAccountExists(transferAccountNum) == 0.0) {
                System.out.println("\nError: Transfer account number provided by you does not exist\n");
            } else if (this.checkIfAccountExists(accountNum) == 0.0) {
                System.out.println("\nError: Account number provided by you does not exist \n");
            } else {
                if (temp_amount > amount) {
                    //update both account balances
                    double transAccountBal = transferAccountBalance + amount;
                    double userAccountBal = temp_amount - amount;
                    this.updateAccountInfo(accountNum, amount, "withdraw");
                    this.updateAccountInfo(transferAccountNum, amount, "deposit");
                    System.out.println("\n======== Successful operation ========");
                    System.out.println("Closing balance of Account number: " + accountNum + " is " + userAccountBal);
                    System.out.println("Closing balance of Account number: " + transferAccountNum + " is " + transAccountBal + "\n");
                } else if (accountNum == transferAccountNum) {
                    System.out.println("\nError: You cannot transfer amount in same account \n");
                } else {
                    System.out.println("\nError: Your account number does not have sufficient balance \n");
                }
            }
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", "MyFile.txt");
            e.printStackTrace();
        }
    }

    private int findMaxId() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("MyFile.txt"));
            int count = 0;
            while ((reader.readLine()) != null) {
                count = count + 1;
            }
            reader.close();
            // Logic for finding maximum Id
            return count / 3;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", "MyFile.txt");
            e.printStackTrace();
        }
        return 0;
    }
//Display the accpunt detials when asked
    private void findDisplay() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("MyFile.txt"));
            String line;
            String trmpaccountNum = Long.toString(accountNum);
            int count = 1;
            System.out.println("\n======== Your Account Information ========\n");
            while ((line = reader.readLine()) != null) {
                if (count > 1) {
                    trmpaccountNum = line;
                }
                if ((line.equals(trmpaccountNum)) && (count < 5)) {
                    switch (count) {
                        case 1:
                            System.out.println("Account Number: " + line);
                            break;
                        case 2:
                            System.out.println("Balance: " + line);
                            break;
                        case 3:
                            System.out.println("Date of opening: " + line);
                            break;
                        case 4:
                            System.out.println("Name of the account holder: " + line + "\n");
                            break;
                    }
                    count = count + 1;
                }
            }
            reader.close();
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", "MyFile.txt");
            e.printStackTrace();
        }
    }

    private void updateAccountInfo(long accountNum, double amount, String type) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("MyFile.txt"));
            FileWriter fstream = new FileWriter("TempFile.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            String line;
            String trmpaccountNum = Long.toString(accountNum);
            int count = 1;
            while ((line = reader.readLine()) != null) {
                if (count > 1) {
                    trmpaccountNum = line;
                }
                if ((line.equals(trmpaccountNum)) && (count < 4)) {
                    double temp_amount = 0;
                    if (count == 1) {
                        out.write(accountNum + "\n");
                    } else if (count == 2) {
                        if (type.equalsIgnoreCase("withdraw")) {
                            //subtracts the withdrawn amount
                            temp_amount = Double.parseDouble(line) - amount;
                        } else if (type.equalsIgnoreCase("deposit")) {
                            //add the deposit amount
                            temp_amount = amount + Double.parseDouble(line);
                        }
                        if (temp_amount < 0) {
                            System.out.println("You do not have sufficient balance \n");
                            out.write(Double.toString(amount) + "\n");
                        } else {
                            out.write(Double.toString(temp_amount) + "\n");
                        }
                    } else if (count == 3) {
                        out.write(date + "\n");
                    }
                    count = count + 1;
                } else {
                    out.write(line + "\n");
                }
            }
            out.close();
            reader.close();
            File f1 = new File("MyFile.txt");
            f1.delete();
            File f2 = new File("TempFile.txt");
            boolean b = f2.renameTo(f1);
            if (b) {
            } else {
                System.out.println("Updated has Error");
            }
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", "MyFile.txt");
            e.printStackTrace();
        }
    }

    private void findUpdate() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("MyFile.txt"));
            FileWriter fstream = new FileWriter("TempFile.txt", true);
            BufferedWriter out = new BufferedWriter(fstream);
            String line;
            String trmpaccountNum = Long.toString(accountNum);
            int count = 1;
            while ((line = reader.readLine()) != null) {
                if (count > 1) {
                    trmpaccountNum = line;
                }
                if ((line.equals(trmpaccountNum)) && (count < 4)) {
                    double temp_amount = 0;
                    if (count == 1) {
                        out.write(accountNum + "\n");
                    } else if (count == 2) {
                        if (transactionType.equalsIgnoreCase("withdraw")) {
                            //subtracts the withdrawn amount
                            temp_amount = Double.parseDouble(line) - amount;
                        } else if (transactionType.equalsIgnoreCase("deposit")) {
                            //add the deposit amount
                            temp_amount = amount + Double.parseDouble(line);
                        }
                        if (temp_amount < 0) {
                            System.out.println("You do not have sufficient balance \n");
                            out.write(Double.toString(amount) + "\n");
                        } else {
                            System.out.println("==== Successful operation ====");
                            System.out.println(Double.toString(temp_amount) + " is your closing balance\n");
                            out.write(Double.toString(temp_amount) + "\n");
                        }
                    } else if (count == 3) {
                        out.write(date + "\n");
                    }
                    count = count + 1;
                } else {
                    out.write(line + "\n");
                }
            }
            out.close();
            reader.close();
            File f1 = new File("MyFile.txt");
            f1.delete();
            File f2 = new File("TempFile.txt");
            boolean b = f2.renameTo(f1);
            if (b) {
            } else {
                System.out.println("Updated has Error");
            }
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", "MyFile.txt");
            e.printStackTrace();
        }
    }
}