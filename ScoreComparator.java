package bejeweled;

import java.util.Comparator;


public class ScoreComparator implements Comparator {
    @Override
    public int compare(Object arg0, Object arg1) {
        Move objArg0 = (Move) arg0;
        Move objArg1 = (Move) arg1;

        if (objArg0.score > objArg1.score) {
            return -1;
        } else if (objArg0.score == objArg1.score) {
            return 0;
        } else {
            return 1;
        }
    }
}
