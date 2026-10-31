package lab01;
import java.util.Scanner;
public class Lab01Demo {
    record BankInfo(String name, String branch) {}
    enum MenuOption { OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT }
    public static void main(String[] args) {
        System.out.println(new BankInfo("MiniBank", "Main Branch"));
        try (Scanner in = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n1 Open account  2 Deposit  3 Withdraw  4 Transfer  0 Exit");
                System.out.print("Choice: ");
                if (!in.hasNextInt()) { in.nextLine(); System.out.println("Enter a number."); continue; }
                int n = in.nextInt();
                String message = switch (n) {
                    case 1 -> "Open account — implemented in the main MiniBank app";
                    case 2 -> "Deposit — implemented in the main MiniBank app";
                    case 3 -> "Withdraw — implemented in the main MiniBank app";
                    case 4 -> "Transfer — implemented in the main MiniBank app";
                    case 0 -> "Goodbye";
                    default -> "Invalid choice";
                };
                System.out.println(message);
                if (n == 0) running = false;
            }
        }
    }
}
