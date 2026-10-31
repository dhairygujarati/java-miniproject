package minibank;
import minibank.exception.*;
import minibank.model.*;
import minibank.service.*;
import minibank.util.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Educational console banking application. Local file persistence only; not production banking software.
 */
public class MiniBank {
    private static final BankInfo BANK = new BankInfo("MiniBank", "Main Branch");
    private static final Path DATA=Path.of("data");
    private static final Path LOG=DATA.resolve("logs").resolve("transactions.log");
    private static final Path STATE=DATA.resolve("accounts.dat");
    private static final Path REPORT=DATA.resolve("report.txt");
    private static final Path GOALS_FILE=DATA.resolve("savings-goals.properties");
    private static final Map<String,Long> SAVINGS_GOALS=new TreeMap<>();
    private static final AccountStore STORE=new AccountStore();
    private static final Scanner IN=new Scanner(System.in);
    private static final DateTimeFormatter TS=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public record BankInfo(String name,String branch){}
    public enum MenuOption { OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, VIEW_ACCOUNTS, SEARCH_ACCOUNT, STATEMENT, CLOSE_ACCOUNT, CUSTOMER_VALIDATION, SORT_ACCOUNTS, REPORT, SAVE, SET_SAVINGS_GOAL, VIEW_SAVINGS_GOAL, EXIT }

    public static void main(String[] args) {
        System.out.println("====================================\n"+BANK.name()+" | "+BANK.branch()+"\nConsole Banking System (Java 17+)\n====================================");
        loadState();
        loadSavingsGoals();
        boolean running=true;
        while(running){
            printMenu();
            int choice=readInt("Choose an option: ");
            MenuOption option = switch(choice){
                case 1 -> MenuOption.OPEN_ACCOUNT; case 2 -> MenuOption.DEPOSIT; case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER; case 5 -> MenuOption.VIEW_ACCOUNTS; case 6 -> MenuOption.SEARCH_ACCOUNT;
                case 7 -> MenuOption.STATEMENT; case 8 -> MenuOption.CLOSE_ACCOUNT; case 9 -> MenuOption.CUSTOMER_VALIDATION;
                case 10 -> MenuOption.SORT_ACCOUNTS; case 11 -> MenuOption.REPORT; case 12 -> MenuOption.SAVE; case 13 -> MenuOption.SET_SAVINGS_GOAL;
                case 14 -> MenuOption.VIEW_SAVINGS_GOAL; case 0 -> MenuOption.EXIT; default -> null;
            };
            if(option==null){System.out.println("Invalid choice. Enter 0 to 14.");continue;}
            try {
                switch(option){
                    case OPEN_ACCOUNT -> openAccount();
                    case DEPOSIT -> deposit();
                    case WITHDRAW -> withdraw();
                    case TRANSFER -> transfer();
                    case VIEW_ACCOUNTS -> viewAccounts();
                    case SEARCH_ACCOUNT -> searchAccount();
                    case STATEMENT -> statement();
                    case CLOSE_ACCOUNT -> closeAccount();
                    case CUSTOMER_VALIDATION -> customerValidation();
                    case SORT_ACCOUNTS -> sortAccounts();
                    case REPORT -> System.out.println(ReportGenerator.generate(DATA.resolve("logs"),REPORT));
                    case SAVE -> {saveState();saveSavingsGoals();System.out.println("Accounts and savings goals saved.");}
                    case SET_SAVINGS_GOAL -> setSavingsGoal();
                    case VIEW_SAVINGS_GOAL -> viewSavingsGoal();
                    case EXIT -> {saveState();saveSavingsGoals();System.out.println("Goodbye. Your local account state was saved.");running=false;}
                }
            } catch (BankException e){System.out.println("Banking operation failed: "+e.getMessage());}
              catch (IOException e){System.out.println("File operation failed: "+e.getMessage());}
              catch (RuntimeException e){System.out.println("Input/operation error: "+e.getMessage());}
            System.out.println();
        }
    }
    private static void printMenu(){
        System.out.println("\n------------ MAIN MENU ------------");
        System.out.println("1. Open account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. View all accounts\n6. Search account\n7. Print statement\n8. Close account\n9. Validate customer details\n10. Sort accounts by balance\n11. Generate report\n12. Save now\n13. Set savings goal\n14. View savings goal progress\n0. Exit");
    }
    /** Set a personal savings target for an existing account. */
    private static void setSavingsGoal() throws BankException, IOException {
        Account account = requireAccount(readText("Account number: "));
        long target = readLong("Savings target in rupees (must be greater than zero): ");
        if (target <= 0) {
            System.out.println("Savings target must be greater than zero.");
            return;
        }
        SAVINGS_GOALS.put(account.getAccountNumber(), target);
        saveSavingsGoals();
        System.out.println("Savings goal saved for " + account.getAccountNumber() + ": ₹" + target);
        showGoalProgress(account, target);
    }

    /** Display how close an account is to its savings target. */
    private static void viewSavingsGoal() throws BankException {
        Account account = requireAccount(readText("Account number: "));
        Long target = SAVINGS_GOALS.get(account.getAccountNumber());
        if (target == null) {
            System.out.println("No savings goal found. Choose option 13 to set one.");
            return;
        }
        showGoalProgress(account, target);
    }

    private static void showGoalProgress(Account account, long target) {
        long balance = account.getBalance();
        double percent = Math.min(100.0, balance * 100.0 / target);
        long remaining = Math.max(0L, target - balance);
        int bars = (int)Math.round(percent / 10.0);
        System.out.println("\n--- SAVINGS GOAL PROGRESS ---");
        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("Current balance: ₹" + balance);
        System.out.println("Target amount: ₹" + target);
        System.out.printf(java.util.Locale.ROOT, "Progress: %.1f%% %s%n", percent, "[" + "#".repeat(bars) + "-".repeat(10-bars) + "]");
        if (remaining == 0) System.out.println("Congratulations! Your savings target has been reached.");
        else System.out.println("Amount left to reach target: ₹" + remaining);
    }

    private static void loadSavingsGoals() {
        if (!Files.exists(GOALS_FILE)) return;
        Properties properties = new Properties();
        try (var input = Files.newInputStream(GOALS_FILE)) {
            properties.load(input);
            for (String key : properties.stringPropertyNames()) {
                try {
                    long target = Long.parseLong(properties.getProperty(key));
                    if (target > 0) SAVINGS_GOALS.put(key, target);
                } catch (NumberFormatException ignored) { /* skip damaged entries */ }
            }
        } catch (IOException e) {
            System.out.println("Could not load savings goals: " + e.getMessage());
        }
    }

    private static void saveSavingsGoals() throws IOException {
        Files.createDirectories(GOALS_FILE.getParent());
        Properties properties = new Properties();
        SAVINGS_GOALS.forEach((account, target) -> properties.setProperty(account, Long.toString(target)));
        try (var output = Files.newOutputStream(GOALS_FILE)) {
            properties.store(output, "MiniBank savings goals - account number to target amount");
        }
    }

    private static void openAccount() throws IOException {
        String name=readText("Account holder full name: ");
        String email=readText("Email: ");String mobile=readText("Mobile (10 digits starting 6-9): ");
        if(!Validator.isValidEmail(email)||!Validator.isValidMobile(mobile)){System.out.println("Invalid email or mobile. Account was not opened.");return;}
        String addressLine=readText("Address line: "),city=readText("City: "),pin=readText("PIN code: ");
        Customer customer=new Customer(name,email,mobile,new Customer.Address(addressLine,city,pin));
        System.out.println("Account type: 1 Savings (4%, minimum balance)  2 Current (0%, overdraft)  3 Fixed deposit (7%, locked)");
        int type=readInt("Type: ");long opening=readLong("Opening balance in whole rupees (0 or more): ");
        if(opening<0){System.out.println("Opening balance cannot be negative.");return;}
        Account account=switch(type){
            case 1 -> new SavingsAccount(name,opening,readLong("Minimum balance to retain: "));
            case 2 -> new CurrentAccount(name,opening,readLong("Overdraft limit (rupees): "));
            case 3 -> new FixedDepositAccount(name,opening);
            default -> null;
        };
        if(account==null){System.out.println("Unknown account type.");return;}
        STORE.add(account);STORE.addHistory("OPEN "+account.getAccountNumber()+" "+opening);
        log("OPEN",account.getAccountNumber(),opening);
        System.out.println("Account created successfully. Customer ID: "+customer.getCustomerId()+"; Account: "+account.getAccountNumber());
        System.out.println(account);
    }
    private static void deposit() throws BankException,IOException {
        Account a=requireAccount(readText("Account number: "));long amount=readLong("Deposit amount (₹): ");a.deposit(amount);
        record("DEPOSIT",a.getAccountNumber(),amount);System.out.println("Deposit successful. New balance: ₹"+a.getBalance());
    }
    private static void withdraw() throws BankException,IOException {
        Account a=requireAccount(readText("Account number: "));long amount=readLong("Withdrawal amount (₹): ");a.withdraw(amount);
        record("WITHDRAW",a.getAccountNumber(),amount);System.out.println("Withdrawal successful. New balance: ₹"+a.getBalance());
    }
    private static void transfer() throws BankException,IOException {
        Account from=requireAccount(readText("From account: "));Account to=requireAccount(readText("To account: "));
        if(from.equals(to))throw new BankException("Source and destination must be different.");
        long amount=readLong("Transfer amount (₹): ");
        Account first=from.getAccountNumber().compareTo(to.getAccountNumber())<0?from:to;
        Account second=first==from?to:from;
        synchronized(first){synchronized(second){
            from.withdraw(amount);
            try{to.deposit(amount);}catch(BankException e){from.deposit(amount);throw e;}
        }}
        record("TRANSFER",from.getAccountNumber()+"->"+to.getAccountNumber(),amount);
        System.out.println("Transfer successful. Source balance ₹"+from.getBalance()+"; destination balance ₹"+to.getBalance());
    }
    private static void viewAccounts(){List<Account> list=STORE.getAll();if(list.isEmpty()){System.out.println("No accounts yet. Choose Open account to create one.");return;}list.stream().sorted().forEach(System.out::println);}
    private static void searchAccount() throws BankException {Account a=requireAccount(readText("Account number: "));System.out.println(a);}
    private static void statement() throws BankException {System.out.println(requireAccount(readText("Account number: ")).statement());}
    private static void closeAccount() throws BankException,IOException {Account a=requireAccount(readText("Account number: "));a.close();log("CLOSE",a.getAccountNumber(),0);System.out.println("Account closed. It remains visible for audit history.");}
    private static void customerValidation(){
        String email=readText("Email: "),mobile=readText("Mobile: "),pan=readText("PAN (optional; blank to skip): "),ifsc=readText("IFSC (optional; blank to skip): ");
        System.out.println("Email valid: "+Validator.isValidEmail(email));System.out.println("Mobile valid: "+Validator.isValidMobile(mobile));
        if(!pan.isBlank())System.out.println("PAN valid: "+Validator.isValidPan(pan.toUpperCase()));
        if(!ifsc.isBlank())System.out.println("IFSC valid: "+Validator.isValidIfsc(ifsc.toUpperCase()));
    }
    private static void sortAccounts(){List<Account> list=STORE.sortedByBalance();if(list.isEmpty())System.out.println("No accounts.");else list.forEach(System.out::println);}
    private static Account requireAccount(String id) throws AccountNotFoundException {Account a=STORE.get(id.trim().toUpperCase());if(a==null)throw new AccountNotFoundException(id);return a;}
    private static void record(String type,String account,long amount) throws IOException {String line=type+" "+account+" "+amount;STORE.addHistory(line);log(type,account,amount);}
    private static void log(String type,String account,long amount) throws IOException {TransactionLog.append(LOG,type+" "+account+" "+TS.format(LocalDateTime.now())+" "+amount);}
    private static void saveState() throws IOException {StatePersister.save(STORE.getAll(),STATE);}
    private static void loadState(){
        if(!Files.exists(STATE))return;
        try{
            for(Account a:StatePersister.load(STATE)){
                STORE.add(a);
                String id=a.getAccountNumber();
                if(id.matches("AC[0-9]+")) Account.ensureCounterAtLeast(Long.parseLong(id.substring(2)));
            }
            System.out.println("Loaded "+STORE.getAll().size()+" saved account(s).");
        }
        catch(Exception e){System.out.println("Saved state could not be loaded: "+e.getMessage());}
    }
    private static String readText(String prompt){System.out.print(prompt);return IN.hasNextLine()?IN.nextLine().trim():"0";}
    private static int readInt(String prompt){while(true){String s=readText(prompt);try{return Integer.parseInt(s);}catch(NumberFormatException e){System.out.println("Please enter a whole number.");}}}
    private static long readLong(String prompt){while(true){String s=readText(prompt);try{return Long.parseLong(s);}catch(NumberFormatException e){System.out.println("Please enter a whole-number amount.");}}}
}
