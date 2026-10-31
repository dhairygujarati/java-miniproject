package minibank.service;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
public final class ReportGenerator {
    private ReportGenerator(){}
    public static String generate(Path logsDir,Path reportFile) throws IOException {
        long deposits=0,withdrawals=0,transfers=0;StringBuilder report=new StringBuilder("MINIBANK END-OF-DAY REPORT\n");
        Files.createDirectories(logsDir);
        try(DirectoryStream<Path> stream=Files.newDirectoryStream(logsDir,"*.log")){
            for(Path p:stream){
                BasicFileAttributes a=Files.readAttributes(p,BasicFileAttributes.class);
                report.append("\nFile: ").append(p.getFileName()).append(" | bytes=").append(a.size()).append(" | modified=").append(a.lastModifiedTime()).append('\n');
                try(BufferedReader reader=Files.newBufferedReader(p,StandardCharsets.UTF_8)){
                    String line;while((line=reader.readLine())!=null){
                        String[] t=line.trim().split("\\s+");if(t.length<3)continue;
                        try{
                            long amount=Long.parseLong(t[t.length-1]);
                            String type = (t.length >= 5 && t[0].matches("\\d{4}-\\d{2}-\\d{2}")) ? t[2] : t[0];
                            if(type.equals("DEPOSIT"))deposits+=amount;
                            else if(type.equals("WITHDRAW"))withdrawals+=amount;
                            else if(type.equals("TRANSFER"))transfers+=amount;
                        }catch(NumberFormatException ignored){}
                    }
                }
            }
        }
        report.append("\nTotal deposits: ₹").append(deposits).append("\nTotal withdrawals: ₹").append(withdrawals)
              .append("\nTotal transfers: ₹").append(transfers).append("\nNet deposits minus withdrawals: ₹").append(deposits-withdrawals).append('\n');
        Files.createDirectories(reportFile.toAbsolutePath().getParent());
        Files.writeString(reportFile,report.toString(),StandardCharsets.UTF_8);
        return report.toString();
    }
}
