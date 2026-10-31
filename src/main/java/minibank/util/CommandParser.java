package minibank.util;
import minibank.model.Command;
import minibank.model.TransactionType;
public final class CommandParser {
    private CommandParser(){}
    public static Command parse(String line){
        if(line==null) throw new IllegalArgumentException("Command is required.");
        String[] p=line.trim().split("\\s+");
        if(p.length!=3) throw new IllegalArgumentException("Expected: DEPOSIT|WITHDRAW|TRANSFER ACCOUNT_NUMBER AMOUNT");
        try { return new Command(TransactionType.valueOf(p[0].toUpperCase()),p[1],Long.parseLong(p[2])); }
        catch(RuntimeException e){throw new IllegalArgumentException("Invalid command type or amount.");}
    }
}
