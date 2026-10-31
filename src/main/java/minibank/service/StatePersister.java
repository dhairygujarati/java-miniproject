package minibank.service;
import minibank.model.Account;
import java.io.*;
import java.nio.file.*;
import java.util.List;
public final class StatePersister {
    private StatePersister(){}
    public static void save(List<Account> accounts, Path file) throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        try(ObjectOutputStream out=new ObjectOutputStream(Files.newOutputStream(file))){out.writeObject(accounts.toArray(new Account[0]));}
    }
    public static Account[] load(Path file) throws IOException,ClassNotFoundException {
        try(ObjectInputStream in=new ObjectInputStream(Files.newInputStream(file))){return (Account[])in.readObject();}
    }
}
