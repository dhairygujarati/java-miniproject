package lab06;
public class Lab06Demo {
    interface Transactable { void deposit(long amount); boolean withdraw(long amount); }
    interface InterestBearing { double balance(); double interestRate(); default double yearlyInterest(){return balance()*interestRate()/100.0;} }
    @FunctionalInterface interface WithdrawRule { boolean allow(long balance,long amount); }
    interface Premium {}
    public static void main(String[] args){WithdrawRule rule=(balance,amount)->amount>0&&amount<=balance;System.out.println("Withdraw allowed: "+rule.allow(1000,250));}
}
