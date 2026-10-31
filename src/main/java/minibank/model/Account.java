package minibank.model;

import minibank.exception.*;
import minibank.model.annotation.Id;
import minibank.model.annotation.Positive;
import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;
    private static long counter = 0;
    @Id private final String accountNumber;
    private final String ownerName;
    @Positive private long balance;
    private boolean active = true;

    protected Account(String ownerName, long openingBalance) {
        if (ownerName == null || ownerName.isBlank()) throw new IllegalArgumentException("Owner name is required.");
        if (openingBalance < 0) throw new IllegalArgumentException("Opening balance cannot be negative.");
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName.trim();
        this.balance = openingBalance;
    }
    private static synchronized String generateAccountNumber() { return String.format("AC%04d", ++counter); }
    public static synchronized void ensureCounterAtLeast(long value) { counter = Math.max(counter, value); }
    public static synchronized void observeAccountNumber(String number) {
        if (number != null && number.matches("AC[0-9]+")) {
            try { counter = Math.max(counter, Long.parseLong(number.substring(2))); }
            catch (NumberFormatException ignored) { }
        }
    }
    public String getAccountNumber(){ return accountNumber; }
    public String getOwnerName(){ return ownerName; }
    public synchronized long getBalance(){ return balance; }
    public synchronized boolean isActive(){ return active; }
    public synchronized void close(){ active=false; }
    protected synchronized void addBalance(long amount) throws BankException {
        ensureActive();
        if (amount <= 0) throw new InvalidAmountException("Amount must be greater than zero.");
        try { balance = Math.addExact(balance, amount); }
        catch (ArithmeticException e) { throw new InvalidAmountException("Balance limit exceeded."); }
    }
    protected synchronized boolean subtractBalance(long amount) throws BankException {
        ensureActive();
        if (amount <= 0) throw new InvalidAmountException("Amount must be greater than zero.");
        if (!canWithdraw(amount)) throw new InsufficientFundsException(amount - Math.max(0, balance));
        balance -= amount;
        return true;
    }
    private void ensureActive() throws BankException { if (!active) throw new BankException("This account is closed."); }
    @Override public synchronized void deposit(long amount) throws BankException { addBalance(amount); }
    @Override public synchronized boolean withdraw(long amount) throws BankException { return subtractBalance(amount); }
    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);
    public String statement(){ return "MINIBANK ACCOUNT STATEMENT\nAccount: "+accountNumber+"\nOwner: "+ownerName+"\nType: "+getClass().getSimpleName()+"\nBalance: ₹"+getBalance()+"\nStatus: "+(isActive()?"ACTIVE":"CLOSED")+"\nInterest rate: "+interestRate()+"%"; }
    @Override public String toString(){ return accountNumber+" | "+ownerName+" | ₹"+getBalance()+" | "+getClass().getSimpleName()+" | "+(isActive()?"ACTIVE":"CLOSED"); }
    @Override public boolean equals(Object o){ return o instanceof Account a && accountNumber.equals(a.accountNumber); }
    @Override public int hashCode(){ return Objects.hash(accountNumber); }
    @Override public int compareTo(Account other){ return accountNumber.compareTo(other.accountNumber); }
}
