package lab12;
import java.util.*;import java.util.concurrent.ConcurrentHashMap;
public class Lab12Demo {
    record Account(String number,long balance) implements Comparable<Account>{public int compareTo(Account a){return number.compareTo(a.number);}}
    static class AccountStore {private final Map<String,Account> accounts=new ConcurrentHashMap<>();private final List<String> history=Collections.synchronizedList(new ArrayList<>());void add(Account a){accounts.put(a.number(),a);}Account get(String id){return accounts.get(id);}List<Account> sortedByBalance(){List<Account> l=new ArrayList<>(accounts.values());l.sort(Comparator.comparingLong(Account::balance).reversed());return l;}}
    public static void main(String[] args){AccountStore s=new AccountStore();System.out.println("AccountStore ready for user-created accounts; no sample dataset is loaded.");}
}
