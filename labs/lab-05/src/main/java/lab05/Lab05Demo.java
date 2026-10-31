package lab05;
abstract class Account { protected long balance; Account(long b){balance=b;} abstract double interestRate(); abstract boolean canWithdraw(long amount); }
class SavingsAccount extends Account { private final long min; SavingsAccount(long b,long m){super(b);min=m;} double interestRate(){return 4.0;} boolean canWithdraw(long a){return a>0&&balance-a>=min;} }
class CurrentAccount extends Account { private final long overdraft; CurrentAccount(long b,long o){super(b);overdraft=o;} double interestRate(){return 0.0;} boolean canWithdraw(long a){return a>0&&balance-a>=-overdraft;} }
class FixedDepositAccount extends Account { FixedDepositAccount(long b){super(b);} double interestRate(){return 7.0;} boolean canWithdraw(long a){return false;} }
public class Lab05Demo { public static void main(String[] args){Account[] a={new SavingsAccount(5000,1000),new CurrentAccount(5000,2000),new FixedDepositAccount(5000)};for(Account x:a)System.out.println(x.getClass().getSimpleName()+" rate="+x.interestRate()+"%");} }
