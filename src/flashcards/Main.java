package flashcards;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Input the number of cards:");
        int number = Integer.parseInt(input());

    }

    private static String input() {
        return scanner.nextLine();
    }
}
