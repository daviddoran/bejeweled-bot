package bejeweled;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;

public class Solver {
    /**
     * Calculate the moves to make
     *
     * @param board The gem board
     * @return The moves to make
     * @throws NoMoveException
     */
    @SuppressWarnings("unchecked")
    public static LinkedList<Move> solve(int[][] board) throws NoMoveException {
        boolean[][] localUsedBoard = new boolean[8][8];
        //List of moves to return
        LinkedList<Move> localMoves = new LinkedList<Move>();
        LinkedList<Move> moves = new LinkedList<Move>();
        //Right and down moves
        Move rightMove, downMove;
        //Initial local scoring
        int rightScore = 0, downScore = 0, moveScore = 0;
        //Max score and the max associate move
        int diffScore = 0;
        //Base score
        int baseScore = Board.scoreAll(board);
        //Max calculated score
        int maxScore = baseScore;

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                rightScore = makeMoveScore(board, rightMove = new Move(j, i, j + 1, i), localUsedBoard);
                downScore = makeMoveScore(board, downMove = new Move(j, i, j, i + 1), localUsedBoard);
                if (Math.max(rightScore, downScore) >= 3) {
                    if (rightScore > downScore) {
                        rightMove.score = rightScore;
                        localMoves.add(rightMove);
                    } else {
                        downMove.score = downScore;
                        localMoves.add(downMove);
                    }
                }
            }
        }

        Collections.sort(localMoves, new ScoreComparator());

        for (Move currentMove : localMoves) {
            moveScore = makeMoveScoreAll(board, currentMove);
            diffScore = moveScore - maxScore;
            if (diffScore > 0) {
                maxScore = moveScore;
                board = Board.swapGems(board, currentMove.x1, currentMove.y1, currentMove.x2, currentMove.y2);
                moves.add(currentMove);
            }
        }

        if (moves.size() > 0) {
            Collections.sort(moves, new InvYComparator());
            return moves;
        }

        throw new NoMoveException();
    }

    /**
     * Scores "around" a given gem.
     *
     *  3
     * 777 <-- +1 for 3 in row.
     *  6
     */
    public static int scorePosition(int[][] board, int x, int y, boolean[][] usedBoard) {
        int x2, y2;
        int score = 0;
        int gem = board[y][x];
        LinkedList<Point> gemBuffer = new LinkedList<Point>();

        //Score leftwards
        gemBuffer.add(new Point(x, y));
        for (x2 = x - 1; x2 >= 0 && board[y][x2] == gem && !usedBoard[y][x2]; x2--) {
            gemBuffer.add(new Point(x2, y));
        }
        //Score rightwards
        for (x2 = x + 1; x2 < 8 && board[y][x2] == gem && !usedBoard[y][x2]; x2++) {
            gemBuffer.add(new Point(x2, y));
        }

        if (gemBuffer.size() >= 3) {
            score += gemBuffer.size();
            for (Point usedPoint : gemBuffer) {
                usedBoard[usedPoint.y][usedPoint.x] = true;
            }
        }

        gemBuffer.clear();
        gemBuffer.add(new Point(x, y));
        //Score upwards
        for (y2 = y - 1; y2 >= 0 && board[y2][x] == gem && !usedBoard[y2][x]; y2--) {
            gemBuffer.add(new Point(x, y2));
        }
        //Score downwards
        for (y2 = y + 1; y2 < 8 && board[y2][x] == gem && !usedBoard[y2][x]; y2++) {
            gemBuffer.add(new Point(x, y2));
        }

        if (gemBuffer.size() >= 3) {
            score += gemBuffer.size();
            for (Point usedPoint : gemBuffer) {
                usedBoard[usedPoint.y][usedPoint.x] = true;
            }
        }

        return score;
    }

    /**
     * Simulate a move (gem swap) and calculate the score of the two gems involved
     *
     * @param board The gem board
     * @param move  The move (gem swap)
     * @param usedBoard The gems that have already been moved
     * @return The calculated score
     */
    public static int makeMoveScore(int[][] board, Move move, boolean[][] usedBoard) {
        if (move.x2 < 0 || move.x2 >= 8 || move.y2 < 0 || move.y2 >= 8 || usedBoard[move.y1][move.x1] || usedBoard[move.y2][move.x2]) {
            return 0;
        }
        int[][] tempBoard = Board.swapGems(board.clone(), move.x1, move.y1, move.x2, move.y2);
        return scorePosition(tempBoard, move.x1, move.y1, usedBoard) + scorePosition(tempBoard, move.x2, move.y2, usedBoard);
    }

    /**
     * Simulate a move (gem swap) and calculate the score of the board
     *
     * @param board The gem board
     * @param move  The move (gem swap)
     * @return The calculated score
     */
    public static int makeMoveScoreAll(int[][] board, Move move) {
        if (move.x2 < 0 || move.x2 >= 8 || move.y2 < 0 || move.y2 >= 8) {
            return 0;
        }
        int[][] tempBoard = Board.swapGems(board.clone(), move.x1, move.y1, move.x2, move.y2);
        return Board.scoreAll(tempBoard);
    }
}
