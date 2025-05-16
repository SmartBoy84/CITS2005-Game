package game;

public class GridImpl implements Grid {
    PieceColour[][] grid;

    public GridImpl(int size) {
        this.grid = new PieceColour[size][size];

        // initalise grid
        for (var row : this.grid)
            for (int i = 0; i < size; i++)
                row[i] = PieceColour.NONE;
    }

    // private method to pre-initialise gridF
    // private: implementation detail of the interface
    private GridImpl(PieceColour[][] grid) {
        this.grid = grid;
    }

    private void testBounds(int row, int col) throws IllegalArgumentException {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid.length) {
            throw new IllegalArgumentException(
                    String.format("(%d,%d) out of bounds; expected range [0, ]", col, row, grid.length - 1));
        }
    }

    // implement Grid interface methods
    // (online I found that "@Override" was frequently used even for
    // abstract/interface method implementations) - this
    // seems reasonable to me as it indicates to reader that the method isn't
    // 'original' to class - so I do this as well (even if not 'needed' per se,
    // since Java will tell me even if I didn't use them that I have methods from
    // the interface which I haven't implemented)

    @Override
    public int getSize() {
        return grid.length; // since "grid is always a square"
    }

    @Override
    public PieceColour getPiece(int row, int col) throws IllegalArgumentException {
        testBounds(row, col);
        return grid[row][col];
    }

    @Override
    public void setPiece(int row, int col, PieceColour piece) {
        testBounds(row, col);
        grid[row][col] = piece;
    }

    @Override
    public Grid copy() {
        var gridClone = new PieceColour[getSize()][];
        for (int x = 0; x < getSize(); x++) {
            // docs: "By convention, the object returned by this method should be
            // independent of this object"
            // => .clone() impl on arrays performs deep copy
            gridClone[x] = grid[x].clone();
        }

        return new GridImpl(gridClone);
    }

    @Override
    public String toString() {
        var s = "";
        for (PieceColour[] row : grid) {
            for (PieceColour entry : row)
                s += entry.pieceName();
            s += "\n";
        }
        return s;
    }
}
