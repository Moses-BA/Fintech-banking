import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import constants.TradeAccountType;
import constants.Transaction;
import pojo.CashAccount;
import pojo.MarginAccount;
import repository.TradeAccountRepository;
import service.CashAccountService;
import service.MarginAccountService;

public class Main {

    static Path[] paths = new Path[] {Paths.get("data/accounts.txt"), Paths.get("data/transactions.txt")};
    static AccountRepository accountRepository = new AccountRepository();
    static CheckingService checkingService = new CheckingService(accountRepository);
    static CreditService creditService = new CreditService(accountRepository);
    static Scanner scan = new Scanner(System.in);
    static String presentID;
    static String account2;

    public static void main(String[] args) {
        try {
            loadAccounts();
            applyTransactions();
            finalTest(); 
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

        System.out.println("\nWelcome to the bank of BEAM.");
        System.out.print("Please enter your name: ");
        String name = scan.nextLine();

        System.out.print("\nHello " + name + ". Do you wish to open an account? (Yes or no) ");
        String choice = scan.nextLine();

        if (choice.equalsIgnoreCase("yes")){
            openAccount(scan);
        } else if (choice.equalsIgnoreCase("no")){
            System.out.println("Thank you for using our bank.");
        } else {
            System.out.println("Invalid choice!");
        }
        
    }

    public static void loadAccounts() throws IOException {
        Files.lines(paths[0])
            .forEach(line -> {
                String[] words = line.split(" ");
                if (words[0].equals(AccountType.CHECKING.toString())) {
                    checkingService.createAccount(new Checking(words[1], new BigDecimal(words[2])));
                } else {
                    creditService.createAccount(new Credit(words[1], new BigDecimal(words[2])));
                }
            });
    }

    public static void applyTransactions() throws IOException {
        Files.lines(paths[1])
            .forEach(line -> {
                String[] words = line.split(" ");
                Boolean isChecking = words[0].equals(AccountType.CHECKING.toString());
                AccountService accountService = isChecking ? checkingService : creditService;
                Transaction transaction = Transaction.valueOf(words[2]);
                if (transaction.equals(Transaction.DEPOSIT)) {
                    accountService.deposit(words[1], new BigDecimal(words[3]));
                } else {
                    accountService.withdraw(words[1], new BigDecimal(words[3]));
                }
        });
    }

    public static void finalTest() throws IOException {
        System.out.println("Account A1234B Balance: " + checkingService.retrieveAccount("A1234B").getBalance());
        System.out.println("Account E3456F Balance: " + checkingService.retrieveAccount("E3456F").getBalance());
        System.out.println("Account I5678J Balance: " + checkingService.retrieveAccount("I5678J").getBalance());
        System.out.println("Account C2345D Credit: " + creditService.retrieveAccount("C2345D").getCredit());
        System.out.println("Account G4567H Credit: " + creditService.retrieveAccount("G4567H").getCredit());
    }

    public static void openAccount(Scanner scan){
        while(true){
            System.out.print("\nWhat account type would you like to open? Choose between cash and margin: ");
            String account = scan.nextLine();
            if( !(account.toUpperCase().equals(TradeAccountType.CASH.toString())  ||   account.toUpperCase().equals(TradeAccountType.MARGIN.toString()))){
                System.out.println("Choose a valid account type.");
                continue;
            } else {
                account2 = account.toUpperCase();
                String id = generateID();
                String upperAccount = account.toUpperCase();
                if(upperAccount.equals(TradeAccountType.CASH.toString())){
                    cashAccountService.createTradeAccount(new CashAccount(id, new BigDecimal(0)));
                } else {
                    marginAccountService.createTradeAccount(new MarginAccount(id, new BigDecimal(0)));
                }
                presentID = id;
                break;
            }
        }

        System.out.print("\nDo you wish to view your account information? (yes or no) ");
        String userChoice = scan.nextLine();

        if (userChoice.equalsIgnoreCase("yes")){

            System.out.print("\nEnter the ID of the trade account that you want information about: ");
            System.out.println("Your ID is " + presentID);
            String id = presentID;

            if(account2.equals(TradeAccountType.CASH.toString())){
                CashAccount cash = cashAccountService.retrieveTradeAccount(id);
                System.out.println("\n Account information: ");
                System.out.println("\tAccount ID: " + cash.getId());
                System.out.println("\tAccount type: " + account2);
                System.out.println("\tAccount balance: " + cash.getCashBalance());
            } else {
                MarginAccount margin = marginAccountService.retrieveTradeAccount(id);
                System.out.println("\n Account information: ");
                System.out.println("\tAccount ID: " + margin.getId());
                System.out.println("\tAccount type: " + account2);
                System.out.println("\tAccount balance: " + margin.getMargin());
            }


        } else if (userChoice.equalsIgnoreCase("no")){
            System.out.println("Delighted to do business with you!");
        } else {
            System.out.println("Invalid input!");
        }

    }

    public static String generateID(){
        String alphabet1 = String.valueOf(generateRandomAlphabet());
        String number = String.valueOf(generateRandomNumber());
        String alphabet2 = String.valueOf(generateRandomAlphabet());
        return alphabet1 + number + alphabet2;
    }

    public static char generateRandomAlphabet() {
        Random random = new Random();
        int randomNumber = random.nextInt(90 - 65 + 1) + 65;
        char randomAlphabet = (char) randomNumber;
        return randomAlphabet;
    }

    public static int generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(1999 - 1000 + 1) + 1000;
        return randomNumber;
    }
    
}
