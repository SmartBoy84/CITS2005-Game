package game;

public enum PieceColour {
    NONE, WHITE, BLACK;

    // not overriding toString() to not mess with default expected public API of
    // enum
    public String pieceName() {
        switch (this) {
            case NONE:
                return ".";
            case WHITE:
                return "W";
            case BLACK:
                return "B";
            default:
                throw new IllegalStateException("Unreachable enum state: " + this); // unreachable state
        }
    }
}