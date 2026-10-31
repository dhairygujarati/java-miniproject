package lab08;
public class Lab08Demo {
    static class BankException extends Exception { BankException(String m){super(m);} }
    static class InsufficientFundsException extends BankException { final long shortfall; InsufficientFundsException(long s){super("Short by "+s);shortfall=s;} }
    static void withdraw(long balance,long amount) throws BankException {if(amount<=0)throw new BankException("Amount must be positive");if(amount>balance)throw new InsufficientFundsException(amount-balance);System.out.println("Withdrawal successful");}
    public static void main(String[] args){try{withdraw(1000,5000);}catch(BankException e){System.out.println("Withdrawal failed: "+e.getMessage());}finally{System.out.println("Operation finished");}}
}
