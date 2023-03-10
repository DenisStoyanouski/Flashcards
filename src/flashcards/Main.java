package flashcards;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final Map<String, String> cards = new LinkedHashMap<>();

    private static final Map<String, Integer> statistics = new HashMap<>();

    private static final StringBuilder log = new StringBuilder();

    private static File file;

    private static String fileImportName;

    private static String fileExportName;

    public static void main(String[] args) {
        getArguments(args.clone());
        startMenu();
    }

    private static void getArguments(String[] clone) {
        if (clone.length == 2) {
            if ("-import".equals(clone[0])) {
                fileImportName = clone[1];
                importCards();
            }
            if ("-export".equals(clone[0])) {
                fileExportName = clone[1];
            }
        }

        if (clone.length == 4) {
            if ("-import".equals(clone[0])) {
                fileImportName = clone[1];
                importCards();
            }
            if ("-export".equals(clone[2])) {
                fileExportName = clone[3];
            }

            if ("-import".equals(clone[2])) {
                fileImportName = clone[3];
                importCards();
            }
            if ("-export".equals(clone[0])) {
                fileExportName = clone[1];
            }
        }
        startMenu();
    }

    private static void startMenu() {
        String item = "";
        do {
            output(String.format("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):%n"));
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
                case "log" : log();
                    break;
                case "hardest card" : getHardestCard();
                    break;
                case "reset stats" : resetStats();
                    break;
                case "exit" :
                    if (fileExportName != null) {
                        exportCards();
                    }
                    output(String.format("Bye bye!%n"));
                    System.exit(0);
                    break;
                default:
                    output(String.format("Unknown command%n"));
                    break;
            }
        } while(!"exit".equals(item));
    }

    private static void resetStats() {
        for (String key : statistics.keySet()) {
            statistics.replace(key, 0);
        }
        output(String.format("Card statistics have been reset.%n"));
    }

    private static void getHardestCard() {
        List<String> hardestCards = new ArrayList<>();
        int max = 0;
        if (!statistics.isEmpty()) {
            max = Collections.max(statistics.values());
            for (var entry : statistics.entrySet()) {
                if (entry.getValue() == max) {
                    hardestCards.add(String.format("\"%s\"", entry.getKey()));
                }
            }
        }

        if (hardestCards.size() == 0 || max == 0) {
            output(String.format("There are no cards with errors.%n"));
        } else if (hardestCards.size() == 1){
            output(String.format("The hardest card is %s. You have %d errors answering it.%n", hardestCards.get(0), max));
        } else {
            output(String.format("The hardest cards are %s. You have %d errors answering them.%n", hardestCards.toString().replaceAll("[]\\[]", ""), max * hardestCards.size()));
        }
    }

    private static void log() {
        output(String.format("File name:%n"));
        String fileName = input();
        file = new File(String.format("./%s", fileName));
        try (FileWriter fileWriter = new FileWriter(file)) {
            output("\n");
            output("\n");
            fileWriter.write(log.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        output(String.format("The log has been saved.%n"));
    }

    private static String input() {
        String input = scanner.nextLine();
        log.append(LocalDateTime.now()).append(" input ").append(input).append("\n");
        return input;
    }

    private static void output(String str) {
        log.append(LocalDateTime.now()).append(" output ").append(str);
        System.out.printf(str);
    }

    private static void addCard() {
        String term;
        String definition;
        output(String.format("the card:%n"));
        term = input();
            if (cards.containsKey(term)) {
                output(String.format("The card \"%s\" already exists.%n", term));
                return;
            }
        output(String.format("The definition of the card:%n"));
        definition = input();
            if (cards.containsValue(definition)) {
                output(String.format("The definition \"%s\" already exists.%n", definition));
                return;
            }
        cards.put(term, definition);
        statistics.put(term, 0);
        output(String.format("The pair (\"%s\":\"%s\") has been added.%n", term, definition));
    }

    private static void remove() {
        output(String.format("Which card?%n"));
        String term = input();
        if (cards.containsKey(term)) {
            cards.remove(term);
            output(String.format("The card has been removed.%n"));
        } else {
            output(String.format("Can't remove \"%s\": there is no such card.%n", term));
        }
    }

    private static void importCards() {
        if (fileImportName == null) {
            output(String.format("File name:%n"));
            fileImportName = input();
        }

        int count = 0;
        try {
            Path p = Paths.get(fileImportName);
            file = new File(String.valueOf(p));
            try(Scanner scan = new Scanner(file)) {
                while(scan.hasNextLine()) {
                    String term = scan.nextLine();
                    String determination = scan.nextLine();
                    int hardness = Integer.parseInt(scan.nextLine().trim());
                    cards.put(term, determination);
                    statistics.put(term, hardness);
                    count++;
                }
                output(String.format("%d cards have been loaded.%n", count));
                System.out.println();
            } catch (FileNotFoundException e) {
                output(String.format("File not found.%n"));
            }
        } catch (InvalidPathException e) {
            output(String.format("File not found.%n"));
        }
        fileImportName = null;
    }

    private static void exportCards() {
        if (fileExportName == null) {
            output(String.format("File name:%n"));
            fileExportName = input();
        }
        file = new File(String.format("./%s", fileExportName));
        int count = cards.size();
        try(FileWriter writer = new FileWriter(file)) {
            for (var entry : cards.entrySet()) {
                writer.write(entry.getKey() + "\n");
                writer.write(entry.getValue() + "\n");
                writer.write(statistics.get(entry.getKey()) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        output(String.format("%d cards have been saved.%n", count));
        fileExportName = null;
    }

    private static void ask() {
        int times = 0;
        output(String.format("How many times to ask?%n"));
        times = Integer.parseInt(input());
        do {
            for (String term : cards.keySet()) {
                if (times == 0) {
                    break;
                }
                output(String.format("Print the definition of \"%s\":%n", term));
                String answer = input();
                if (Objects.equals(answer, cards.get(term))) {
                    output(String.format("Correct!%n"));
                } else if (cards.containsValue(answer)) {
                    String termSecond = null;
                    for (var entry : cards.entrySet()) {
                        if (Objects.equals(entry.getValue(), answer)) {
                            termSecond = entry.getKey();
                        }
                    }
                    output(String.format("Wrong. The right answer is \"%s\", but your definition is correct " +
                            "for \"%s\"%n", cards.get(term), termSecond));
                    statistics.put(term, statistics.get(term) + 1);
                } else {
                    output(String.format("Wrong. The right answer is \"%s\"%n", cards.get(term)));
                    statistics.put(term, statistics.get(term) + 1);
                }
                times--;
            }
        } while(times != 0);
    }
}
