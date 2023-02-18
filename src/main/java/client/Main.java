package client;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Terminal terminal = new Terminal();
            while (true) {
                terminal.refresh();
                terminal.start();
                System.out.print("\nContinue? (y/n)\n>");
                String ans = new Scanner(System.in).nextLine().trim().toLowerCase();
                if (!ans.equals("y")) {
                    break;
                }
            }
        } catch (NullPointerException e) {
            System.exit(-1);
        } catch (NoSuchElementException e) {
            System.exit(0);
        }
    }

}
