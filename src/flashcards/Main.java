package flashcards;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static int number = 0;

    static String[][] cards;

    public static void main(String[] args) {
        getNumber();
        createCards();
        checkCards();
    }

    private static void getNumber() {
        System.out.println("Input the number of cards:");
        number = Integer.parseInt(input());
    }

    private static String input() {
        return scanner.nextLine();
    }

    private static void createCards() {

        cards = new String[number][2];
        int count = 1;
        for (String[] card : cards) {
            System.out.printf("Card #%d%n", count);
            card[0] = input();
            System.out.printf("The definition for card #%d:%n", count);
            card[1] = input();
            count++;
        }
    }

    private static void checkCards() {
        for (String[] card : cards) {
            System.out.printf("Print the definition of \"%s\":%n", card[0]);
            String answer = input();
            if (Objects.equals(answer, card[1])) {
                System.out.println("Correct!");
            } else {
                System.out.printf("Wrong. The right answer is \"%s\"%n", card[1]);
            }
        }
    }
}
