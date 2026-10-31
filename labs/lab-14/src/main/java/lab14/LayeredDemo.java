package lab14;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Small, dependency-free example of model/service/console layers for Practical 14. */
public class LayeredDemo {
    record Entry(String description) { }

    static final class EntryService {
        private final List<Entry> entries = new ArrayList<>();
        void add(String description) {
            if (description == null || description.isBlank()) {
                throw new IllegalArgumentException("Description cannot be blank.");
            }
            entries.add(new Entry(description.trim()));
        }
        int count() { return entries.size(); }
        List<Entry> all() { return List.copyOf(entries); }
    }

    public static void main(String[] args) {
        EntryService service = new EntryService();
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Add entry  2. List entries  3. Count  0. Exit");
                System.out.print("Choice: ");
                String choice = in.nextLine().trim();
                try {
                    switch (choice) {
                        case "1" -> { System.out.print("Description: "); service.add(in.nextLine()); System.out.println("Added."); }
                        case "2" -> service.all().forEach(System.out::println);
                        case "3" -> System.out.println("Count: " + service.count());
                        case "0" -> { System.out.println("Goodbye."); return; }
                        default -> System.out.println("Invalid choice.");
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("Validation error: " + ex.getMessage());
                }
            }
        }
    }
}
