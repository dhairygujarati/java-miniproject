package lab04;
import java.util.regex.Pattern;
public class Lab04Demo {
    enum TransactionType { DEPOSIT, WITHDRAW, TRANSFER }
    record Command(TransactionType type,String accountNumber,long amount) {}
    static boolean validMobile(String s){return s!=null&&Pattern.matches("[6-9][0-9]{9}",s);}
    static boolean validEmail(String s){return s!=null&&Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",s);}
    static Command parse(String line){String[] p=line.trim().split("\\s+");if(p.length!=3)throw new IllegalArgumentException("Expected TYPE ACCOUNT AMOUNT");return new Command(TransactionType.valueOf(p[0]),p[1],Long.parseLong(p[2]));}
    static String statement(String account,long balance){return new StringBuilder("Account: ").append(account).append("\nBalance: ₹").append(balance).toString();}
    public static void main(String[] args){System.out.println("Mobile valid: "+validMobile("9876543210"));System.out.println(parse("DEPOSIT AC0001 500"));System.out.println(statement("AC0001",500));}
}
