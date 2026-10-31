package lab10;
import java.util.concurrent.*;
public class Lab10Demo {
    static class TransactionProcessor implements AutoCloseable {private final ExecutorService pool=Executors.newFixedThreadPool(4);void submit(Runnable r){pool.execute(r);}public void close()throws InterruptedException{pool.shutdown();if(!pool.awaitTermination(10,TimeUnit.SECONDS))pool.shutdownNow();}}
    public static void main(String[] args)throws Exception{try(TransactionProcessor p=new TransactionProcessor()){for(int i=1;i<=8;i++){final int n=i;p.submit(()->System.out.println(Thread.currentThread().getName()+" processed task "+n));}}}
}
