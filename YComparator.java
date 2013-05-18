package bejeweled;

import java.util.Comparator;

public class YComparator implements Comparator {
    @Override
    public int compare(Object arg0, Object arg1) {
        Move objArg0 = (Move) arg0;
        Move objArg1 = (Move) arg1;

        int m0max = Math.max(objArg0.y1, objArg0.y2);
        int m1max = Math.max(objArg1.y1, objArg1.y2);

        if (m0max < m1max) {
            return -1;
        } else if (m0max == m1max) {
            return 0;
        } else {
            return 1;
        }
    }
}
