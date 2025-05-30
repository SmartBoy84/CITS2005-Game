package game.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import game.*;

public class GameTest extends Test {
    public static boolean didThrowException(Runnable action) {
        try {
            action.run();
            return false;
        } catch (Throwable e) {
            return true;
        }
    }

    public static ArrayList<String> getComparableMoveList(Collection<Move> moves) {
        // not ideal; but the Game/Move interfaces are essentially immutable in the
        // context of this assignment
        ArrayList<String> moveSet = new ArrayList<>();
        for (Move m : moves)
            moveSet.add(String.format("%d,%d", m.getCol(), m.getRow()));
        // we are instructed to implement toString(), but that method is not strictly
        // part of interface so manually create string representation here

        return moveSet;
    }

    public static void makeMoves(Game game, int[][] whiteMoves, int[][] blackMoves) {
        for (int i = 0; i < whiteMoves.length; i++) {
            // only for use in tests >8 - at this point all the behaviour requisite for the
            // following as been tested
            game.makeMove(new MoveImpl(whiteMoves[i][0], whiteMoves[i][1])); // white move
            game.makeMove(new MoveImpl(blackMoves[i][0], blackMoves[i][1])); // black move
        }
    }

    public static void main(String[] args) {

        // idea for tests: building blocks - build up the basic guarantees first, then
        // advance to more complex tests which rely on earlier assertions

        // 1. verify grid size - constructor
        // these are the given tests, re-implemented using lambda
        System.out.println("\n---- Testing grid size assertions");

        expect(true, didThrowException(() -> new GameImpl(0)));
        expect(false, didThrowException(() -> new GameImpl(5)));

        // 2. test game initialisation conditions
        System.out.println("\n---- Testing game initial parameters correctness");

        var game = new GameImpl(10);

        expect(10, game.getGrid().getSize());
        expect(false, game.isOver());
        expect(PieceColour.NONE, game.winner());
        expect(PieceColour.WHITE, game.currentPlayer());

        // 3. test initial grid - getGrid()
        System.out.println("\n---- Testing grid initialisation");

        for (int row = 0; row < 10; row++)
            for (int col = 0; col < 10; col++)
                expect(PieceColour.NONE, game.getGrid().getPiece(row, col));

        // testing grid deep-copy
        var grid = game.getGrid();
        game.makeMove(new MoveImpl(3, 4));
        expect(PieceColour.NONE, grid.getPiece(3, 4));
        // this is sufficient, as can assume that grid implementation is valid

        // 4. test turns system - makeMove()
        System.out.println("\n---- Testing turns system");

        // WHITE in (3,4) due to move made in 3.
        expect(PieceColour.WHITE, game.getGrid().getPiece(3, 4));
        expect(PieceColour.BLACK, game.currentPlayer());

        game.makeMove(new MoveImpl(1, 2));
        expect(PieceColour.BLACK, game.getGrid().getPiece(1, 2));
        expect(PieceColour.WHITE, game.currentPlayer());

        // 5. test exceptions for illegal moves - makeMove() exceptions
        System.out.println("\n---- Testing illegal moves");
        int[][] alreadyTaken = {
                { 3, 4 }, { 3, 4 } };

        int[][] outOfBounds = {
                { 10, 0 }, // far left
                { 10, 0 }, // far right
                { 0, -10 }, // far top
                { 0, 10 }, // far bottom
                { 10, 10 } // bad diagonal access
        };

        for (int[] move : alreadyTaken)
            expect(true, didThrowException(() -> game.makeMove(new MoveImpl(move[0], move[1]))));

        for (int[] move : outOfBounds)
            expect(true, didThrowException(() -> game.makeMove(new MoveImpl(move[0], move[1]))));

        // 6. Valid moves list - getMoves()
        System.out.println("\n---- Testing correct moves list output");

        var game2 = new GameImpl(2);
        var moves = game2.getMoves();

        expect(moves.size(), 4); // new gameImpl object so should "re-develop" guarantee for this

        ArrayList<String> moveSet = getComparableMoveList(moves);

        int[][] expectedMoves = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
        for (int[] move : expectedMoves)
            expect(1, Collections.frequency(moveSet, String.format("%d,%d", move[0], move[1])));
        // test two things at once - 1. move appears, and 2. only appears once
        // refer to getComparableMoveList method definition for rationale of why
        // toString not used

        // 7. test copy behaviour - copy()
        System.out.println("\n---- Testing game copying");
        int[][] randomMoves = { { 1, 2 }, { 3, 1 }, { 4, 5 }, { 6, 7 } };
        // random set of moves to verify correct copying

        Game game3 = new GameImpl(8);
        for (int[] move : randomMoves)
            game3.makeMove(new MoveImpl(move[0], move[1]));

        Game copiedGame = game3.copy();
        /*
         * test assumes that typeof the copied instance is GameImpl so tests 1-6 do not
         * need to be redone for it
         */

        // initial status of both should be identical
        expect(game3.currentPlayer(), copiedGame.currentPlayer());
        expect(game3.winner(), copiedGame.winner());

        var originalMoves = getComparableMoveList(game3.getMoves());
        var copiedMoves = getComparableMoveList(copiedGame.getMoves());
        expect(true, originalMoves.equals(copiedMoves));

        // now, alter status of either to ensure change isn't reflected in other
        copiedGame.makeMove(new MoveImpl(0, 0));
        expect(PieceColour.NONE, game3.getGrid().getPiece(0, 0)); // change in copy shouldn't reflect in original

        game3.makeMove(new MoveImpl(1, 1));
        expect(PieceColour.NONE, copiedGame.getGrid().getPiece(1, 1)); // change in original shouldn't reflect in copy

        originalMoves = getComparableMoveList(game3.getMoves());
        copiedMoves = getComparableMoveList(copiedGame.getMoves());
        expect(false, originalMoves.equals(copiedMoves));

        // finally, the separation of end states (independent of winner() and isOver())
        // is tested in 8, below

        // 8. test end game behaviour - winner(), isOver()
        System.out.println("---- Testing end game behaviour");

        var game4 = new GameImpl(4); // used for testing white win and draw
        var game5 = new GameImpl(4); // used for testing black win

        expect(false, game4.isOver());
        expect(false, game5.isOver());

        // below is a valid game state for 4x4, with two remaining spots
        // WHITE WIN: {0,3}, BLACK WIN: {3,3}
        makeMoves(game4,
                new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 1 }, { 1, 3 }, { 2, 0 }, { 2, 2 } }, // white moves
                new int[][] { { 1, 0 }, { 1, 2 }, { 2, 1 }, { 2, 3 }, { 3, 0 }, { 3, 1 }, { 3, 2 } } // black moves
        );
        // note: cannot test black win with this moveset since WHITE moves after move
        // set concludes

        // many remaining spots for this move set, but one move for black to win
        // BLACK WIN: {0,2}, WHITE WIN: unimportant (will take >= 2 moves)
        makeMoves(game5,
                new int[][] { { 0, 0 }, { 2, 3 }, { 3, 3 } }, // white moves
                new int[][] { { 1, 2 }, { 2, 2 }, { 3, 2 } } // black moves
        );

        expect(false, game4.isOver());
        expect(false, game5.isOver());

        // test white win
        System.out.println("---- Testing white win");

        var endGameCopy1 = game4.copy();
        endGameCopy1.makeMove(new MoveImpl(0, 3)); // win condition for white

        expect(true, endGameCopy1.isOver());
        expect(PieceColour.WHITE, endGameCopy1.winner());

        // check that white won state doesn't reflect in original game
        expect(false, game4.isOver());
        expect(PieceColour.NONE, game4.winner());

        // test draw
        System.out.println("---- Testing draw");

        var endGameCopy2 = game4.copy();
        endGameCopy2.makeMove(new MoveImpl(3, 3)); // make white occupy winning spot for black
        expect(false, endGameCopy2.isOver()); // not drawn yet - one move still remaining

        endGameCopy2.makeMove(new MoveImpl(0, 3)); // move black into remaining spot

        expect(true, endGameCopy2.isOver());
        expect(PieceColour.NONE, endGameCopy2.winner());

        // check that drawn state doesn't reflect in original game
        expect(false, game4.isOver());
        expect(PieceColour.NONE, game4.winner());

        // test black win
        System.out.println("---- Testing black win");

        game5.makeMove(new MoveImpl(1, 1)); // make random move to pass WHITE turn
        Game endGameCopy3 = game5.copy();

        game5.makeMove(new MoveImpl(0, 2)); // do winning move for BLACK

        expect(true, game5.isOver());
        expect(PieceColour.BLACK, game5.winner());

        // check that 'black won' state doesn't reflect in original game
        expect(false, endGameCopy3.isOver());
        expect(PieceColour.NONE, endGameCopy3.winner());

        // --- END OF TESTS

        checkAllTestsPassed();
    }
}
