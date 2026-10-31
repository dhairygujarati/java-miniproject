package minibank.service;
import minibank.model.Account;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class AccountStore {
    private final Map<String,Account> accounts=new ConcurrentHashMap<>();
    private final List<String> history=Collections.synchronizedList(new ArrayList<>());
    public void add(Account a){Account.observeAccountNumber(a.getAccountNumber());accounts.put(a.getAccountNumber(),a);}
    public Account get(String id){return accounts.get(id);}
    public List<Account> getAll(){return new ArrayList<>(accounts.values());}
    public List<Account> sortedByBalance(){List<Account> list=getAll();list.sort(Comparator.comparingLong(Account::getBalance).reversed());return list;}
    public void addHistory(String line){history.add(line);}
    public List<String> getHistory(){synchronized(history){return new ArrayList<>(history);}}
}
