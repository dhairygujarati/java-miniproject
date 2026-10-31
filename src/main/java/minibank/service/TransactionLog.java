package minibank.service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
public final class TransactionLog {
    private TransactionLog(){}
    public static synchronized void append(Path file,String line) throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        Files.writeString(file,line+System.lineSeparator(),StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.APPEND);
    }
}
