package lab11;
import java.io.*;import java.nio.file.*;import java.nio.charset.StandardCharsets;
public class Lab11Demo {
    record Account(String number,long balance) implements Serializable {private static final long serialVersionUID=1L;}
    public static void main(String[] args)throws Exception{Path dir=Path.of("lab11-data");Files.createDirectories(dir);Path state=dir.resolve("accounts.dat");try(ObjectOutputStream out=new ObjectOutputStream(Files.newOutputStream(state))){out.writeObject(new Account("AC0001",1000));}try(ObjectInputStream in=new ObjectInputStream(Files.newInputStream(state))){System.out.println("Reloaded: "+in.readObject());}Files.writeString(dir.resolve("transactions.log"),"DEPOSIT AC0001 500"+System.lineSeparator(),StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.APPEND);System.out.println("Saved state and appended transaction log.");}
}
