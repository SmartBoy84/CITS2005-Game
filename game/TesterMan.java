package game;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TesterMan {
    public static void main(String[] args) {
        var game = new GameImpl(3);
        do {
            System.out.println(game.getGrid().toString());
            var moves = game.getMoves().toArray();
            for (int i = 0; i < moves.length; i++) {
                System.out.printf("%d: (%d, %d)\n", i, ((Move) moves[i]).getCol(), ((Move) moves[i]).getRow());
            }
            Scanner sc = new Scanner(System.in);
            try {
                int moveId = sc.nextInt();
                // Move move = (Move) moves[moveId - 1];
                System.out.printf("Moving (%d) -> (%d, %d)\n", moveId,((Move) moves[moveId]).getCol(),
                        ((Move) moves[moveId]).getRow());
                game.makeMove((Move) moves[moveId]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Invalid move. Please enter a valid move number.");
                continue;
            } catch (InputMismatchException e) {
                sc.next(); // Clear the mismatched input from the scanner
                System.out.println("Invalid move. Please enter a valid move number.");
                continue;
            }
        } while (!game.isOver());
    }
}
