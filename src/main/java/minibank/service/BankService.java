package minibank.service;

import minibank.exception.AccountNotFoundException;
import minibank.exception.BankException;
import minibank.model.Account;
import minibank.util.Validator;
import java.util.List;

/** Service-layer facade for the MiniBank domain operations (Practical 14). */
public final class BankService {
    private final Repository<Account> repository;

    public BankService(Repository<Account> repository) {
        this.repository = java.util.Objects.requireNonNull(repository, "repository");
    }

    public Account openAccount(Account account) {
        return repository.save(java.util.Objects.requireNonNull(account, "account"));
    }

    public void validateCustomerDetails(String email, String mobile) {
        if (!Validator.isValidEmail(email)) throw new IllegalArgumentException("Invalid email format.");
        if (!Validator.isValidMobile(mobile)) throw new IllegalArgumentException("Invalid mobile number.");
    }

    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        Account account = repository.findById(accountNumber);
        if (account == null) throw new AccountNotFoundException(accountNumber);
        return account;
    }

    public void deposit(String accountNumber, long amount) throws BankException {
        findAccount(accountNumber).deposit(amount);
    }

    public void withdraw(String accountNumber, long amount) throws BankException {
        findAccount(accountNumber).withdraw(amount);
    }

    public void transfer(String fromNumber, String toNumber, long amount) throws BankException {
        Account from = findAccount(fromNumber);
        Account to = findAccount(toNumber);
        if (from.equals(to)) throw new BankException("Source and destination must be different.");
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                try { to.deposit(amount); }
                catch (BankException ex) { from.deposit(amount); throw ex; }
            }
        }
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return findAccount(accountNumber).statement();
    }

    public List<Account> listAccounts() { return repository.findAll(); }
}
