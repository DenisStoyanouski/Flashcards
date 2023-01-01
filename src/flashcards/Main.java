package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final Map<String, String> cards = new LinkedHashMap<>();

    private static File file;

    public static void main(String[] args) {
        startMenu();


    }

    private static void startMenu() {
        String item = "";
        do {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            item = input();
            switch (item) {
                case "add" : addCard();
                    break;
                case "remove" : remove();
                    break;
                case "import" : importCards();
                    break;
                case "export" : exportCards();
                    break;
                case "ask" : ask();
                    break;
                case "exit" :
                    System.out.println("Bye bye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        } while(!"exit".equals(item));
    }

    private static String input() {
        return scanner.nextLine();
    }

    private static void addCard() {
        String term;
        String definition;
        System.out.println("the card:");
        term = input();
            if (cards.containsKey(term)) {
                System.out.printf("The term \"%s\" already exists.%n", term);
                return;
            }
        System.out.println("The definition of the card:");
        definition = input();
            if (cards.containsValue(definition)) {
                System.out.printf("The definition \"%s\" already exists.%n", definition);
                return;
            }
        cards.put(term, definition);
        System.out.printf("The pair (\"%s\":\"%s\") has been added.%n", term, definition);
    }

    private static void remove() {
        System.out.println("Which card?");
        String term = input();
        if (cards.containsKey(term)) {
            cards.remove(term);
            System.out.println("The card has been removed.");
        } else {
            System.out.printf("Can't remove \"%s\": there is no such card.%n", term);
        }
    }

    private static void importCards() {
        System.out.println("File name:");
        String fileName = input();
        int count = 0;
        try {
            Path p = Paths.get(fileName);
            file = new File(String.valueOf(p));
            try(Scanner scan = new Scanner(file)) {
                while(scan.hasNextLine()) {
                    String term = scan.nextLine();
                    String determination = scan.nextLine();
                    cards.put(term, determination);
                    count++;
                }
                System.out.printf("%d cards have been loaded.%n", count);
                System.out.println();
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
            }
        } catch (InvalidPathException e) {
            System.out.println("File not found.");
        }

    }

    private static void exportCards() {
        System.out.println("File name:");
        String fileName = input();
        file = new File(String.format("./%s", fileName));
        int count = cards.size();
        try(FileWriter writer = new FileWriter(file)) {
            for (var entry : cards.entrySet()) {
                writer.write(entry.getKey() + "\n");
                writer.write(entry.getValue() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("%d cards have been saved.%n", count);
    }

    private static void ask() {
        int times = 0;
        System.out.println("How many times to ask?");
        times = Integer.parseInt(input());
        do {
            for (String term : cards.keySet()) {
                if (times == 0) {
                    break;
                }
                System.out.printf("Print the definition of \"%s\":%n", term);
                String answer = input();
                if (Objects.equals(answer, cards.get(term))) {
                    System.out.println("Correct!");
                } else if (cards.containsValue(answer)) {
                    String termSecond = null;
                    for (var entry : cards.entrySet()) {
                        if (Objects.equals(entry.getValue(), answer)) {
                            termSecond = entry.getKey();
                        }
                    }
                    System.out.printf("Wrong. The right answer is \"%s\", but your definition is correct " +
                            "for \"%s\"%n", cards.get(term), termSecond);
                } else {
                    System.out.printf("Wrong. The right answer is \"%s\"%n", cards.get(term));
                }
                times--;
            }
        } while(times != 0);
    }
}
