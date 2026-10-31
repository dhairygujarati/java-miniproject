package lab09;
public class Lab09Demo {
    static class Account { private long balance; synchronized void deposit(long a){balance+=a;} synchronized long balance(){return balance;} }
    static class AccountWorker implements Runnable {private final Account account;private final int times;private final long amount;AccountWorker(Account a,int t,long n){account=a;times=t;amount=n;}public void run(){for(int i=0;i<times;i++)account.deposit(amount);}}
    public static void main(String[] args)throws InterruptedException{Account a=new Account();Thread[] ts=new Thread[10];for(int i=0;i<ts.length;i++){ts[i]=new Thread(new AccountWorker(a,1000,1),"deposit-"+i);ts[i].start();}for(Thread t:ts)t.join();System.out.println("Expected balance 10000; actual="+a.balance());}
}
