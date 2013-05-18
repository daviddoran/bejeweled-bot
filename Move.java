package bejeweled;

public class Move {
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    public int score;

    public Move(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public String toString() {
        return "(" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ")";
    }
}
