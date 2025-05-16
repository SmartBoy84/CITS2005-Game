package game;

import java.util.ArrayList;
import java.util.Collection;

public class GameImpl implements Game {
    private Grid grid; // game grid
    private PieceColour currPlayer = PieceColour.WHITE; // "white moves first"

    public GameImpl(int size) throws IllegalArgumentException {
        if (size <= 0)
            throw new IllegalArgumentException("Grid size must be >= 1 (received " + size + ")");

        this.grid = new GridImpl(size);
    }

    // note all non-interface methods are marked private - they are implementation
    // details of the interface

    // add method to initialise new game with given grid
    // assume reference to pre-existing grid provided, rather than deep copy so
    // .copy() method called here
    private GameImpl(Grid grid, PieceColour currPlayer) {
        this.grid = grid.copy();
        this.currPlayer = currPlayer;
    }

    // check if given pieceColour has won using provided external methods
    private boolean checkWinner(PieceColour pieceColour) {
        return PathFinder.leftToRight(grid, pieceColour) || PathFinder.topToBottom(grid, pieceColour);
    }

    private boolean isOutOfMoves() {
        return getMoves().size() == 0;
    }

    // check if a move is valid
    private boolean moveIsValid(int row, int col) {
        // if move is out-of-bounds, IllegalArgumentException thrown by getPiece
        return grid.getPiece(row, col) == PieceColour.NONE;
    }

    // ----- implement Game interface methods -----

    // True if the game is over
    @Override
    public boolean isOver() {
        return isOutOfMoves() || checkWinner(PieceColour.WHITE) || checkWinner(PieceColour.BLACK);
    }

    // The colour of the winner.
    @Override
    public PieceColour winner() {
        // if game ended in draw, currPlayer will be set to PieceColour.NONE
        if (checkWinner(PieceColour.WHITE))
            return PieceColour.WHITE;
        else if (checkWinner(PieceColour.BLACK))
            return PieceColour.BLACK;
        else
            return PieceColour.NONE; // drawn, or ongoing
    }

    // The colour of the current player (the player who will make the next move)
    @Override
    public PieceColour currentPlayer() {
        return currPlayer;
    }

    // Gets a Collection of all valid moves for the current player
    @Override
    public Collection<Move> getMoves() {
        var moves = new ArrayList<Move>();

        for (int row = 0; row < grid.getSize(); row++)
            for (int col = 0; col < grid.getSize(); col++)
                if (moveIsValid(row, col))
                    moves.add(new MoveImpl(row, col));

        return moves;
    }

    // Executes a move for the current player
    @Override
    public void makeMove(Move move) throws IllegalArgumentException {

        /*
         * the behaviour of this function is undefined, given that a winning state is
         * reached
         * the wording is slightly ambiguous, but I interpret it as meaning that I do
         * not need to handle persistence of winning state, once reached
         * so it is possible for white to win, but as my function permits black to then
         * move - the game could become drawn but that second state is undefined
         * behaviour
         */

        if (!moveIsValid(move.getRow(), move.getCol())) {
            throw new IllegalArgumentException(String.format("(%d,%d) already occupied", move.getCol(), move.getRow()));
        }
        grid.setPiece(move.getRow(), move.getCol(), currPlayer);

        currPlayer = currPlayer == PieceColour.WHITE ? PieceColour.BLACK : PieceColour.WHITE;
    }

    // Returns a copy of the grid
    @Override
    public Grid getGrid() {
        return grid.copy();
    }

    // Returns a copy of the game
    @Override
    public Game copy() {
        return new GameImpl(grid, currPlayer);
    }
}
