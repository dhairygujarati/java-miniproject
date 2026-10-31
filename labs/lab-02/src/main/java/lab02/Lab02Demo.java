package lab02;
import java.util.concurrent.atomic.AtomicLong;
public class Lab02Demo {
    static class Customer {
        private static final AtomicLong NEXT = new AtomicLong(100);
        private final String customerId = "CUST" + NEXT.incrementAndGet();
        private final String name, email, mobile;
        Customer(String name, String email, String mobile) { this.name=name; this.email=email; this.mobile=mobile; }
        public String getCustomerId(){return customerId;} public String getName(){return name;}
        public String getEmail(){return email;} public String getMobile(){return mobile;}
    }
    static class Account {
        private static final AtomicLong NEXT = new AtomicLong();
        private final String accountNumber = String.format("AC%04d", NEXT.incrementAndGet());
        private final String ownerName; private long balance; private boolean active=true;
        Account(String ownerName){this(ownerName,0);}
        Account(String ownerName,long opening){if(opening<0)throw new IllegalArgumentException("Negative opening balance");this.ownerName=ownerName;this.balance=opening;}
        public synchronized boolean deposit(long amount){if(!active||amount<=0)return false;balance=Math.addExact(balance,amount);return true;}
        public synchronized boolean withdraw(long amount){if(!active||amount<=0||amount>balance)return false;balance-=amount;return true;}
        public long getBalance(){return balance;} public String getAccountNumber(){return accountNumber;}
        public String getOwnerName(){return ownerName;}
    }
    public static void main(String[] args){System.out.println("Lab 02 classes are demonstrated; use the main MiniBank app for interactive banking.");}
}
