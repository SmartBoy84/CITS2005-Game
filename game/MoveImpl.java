package game;

public class MoveImpl implements Move {
    private int row;
    private int col;

    public MoveImpl(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // implement Move interface methods (marker; look in GridImpl for reasoning of '@Override' usage)
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    // override Object toString method
    @Override
    public String toString() {
        return String.format("(%d,%d)", this.row, this.col);
    }
}
