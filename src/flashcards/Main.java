package flashcards;

import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static int number = 0;

    static Map<String, String> cards = new LinkedHashMap<>();

    public static void main(String[] args) {
        startMenu();


    }

    private static void startMenu() {
        String item = "";
        do {
            System.out.println("Input the action (add, remove, import, export, ask, exit):");
            item = input();
            switch (item) {
                case "add" : ;
                    break;
                case "remove" : ;
                    break;
                case "import" : ;
                    break;
                case "export" : ;
                    break;
                case "ask" : ;
                    break;
                case "exit" : ;
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }
        } while(!"exit".equals(item));
    }

    private static void getNumber() {
        System.out.println("Input the number of cards:");
        number = Integer.parseInt(input());
        createCards();
    }

    private static String input() {
        return scanner.nextLine();
    }

    private static void createCards() {
        int count = 1;
        String term = null;
        String definition = null;
        while (number != 0) {
            System.out.printf("Card #%d%n", count);
            do {
                term = input();
                if (cards.containsKey(term)) {
                    System.out.printf("The term \"%s\" already exists. Try again:%n", term);
                }
            } while (cards.containsKey(term));

            System.out.printf("The definition for card #%d:%n", count);
            do {
                definition = input();
                if (cards.containsValue(definition)) {
                    System.out.printf("The definition \"%s\" already exists. Try again:%n", definition);
                }
            } while (cards.containsValue(definition));
            cards.put(term, definition);
            count++;
            number--;
        }
        checkCards();
    }

    private static void checkCards() {
        for (String term : cards.keySet()) {
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
        }
    }
}
