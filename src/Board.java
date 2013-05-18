package bejeweled;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Board {
    public static final String[] colors = {
            "WHITE",
            "YELLOW",
            "ORANGE",
            "BLUE",
            "GREEN",
            "PURPLE",
            "RED"
    };

    private int[] idealGemColors = {
            -1118482,  //WHITE,
            -67548,    //YELLOW,
            -1070259,  //ORANGE,
            -15629833, //BLUE,
            -13448362, //GREEN,
            -848141,   //PURPLE,
            -320713    //RED
    };

    private int cubeSize;
    private int halfCubeSize;
    private int sampleSize;
    private int sampleArea;

    public Board(int cubeSize, int sampleSize) {
        this.cubeSize = cubeSize;
        this.halfCubeSize = cubeSize / 2;
        this.sampleSize = sampleSize;
        this.sampleArea = (int) Math.pow(sampleSize, 2);
    }

    public static String getColorName(int colorNumber) {
        return colors[colorNumber];
    }

    /**
     * Determine the colours of the gems on the board
     *
     * @param screen The board screenshot
     * @return A 2D array of the colours of the gems
     */
    public int[][] getBoardGemColors(BufferedImage screen) {
        BufferedImage sample;
        Color averageColor;
        int[][] gemColors = new int[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int gemCenterX = halfCubeSize + cubeSize * j;
                int gemCenterY = halfCubeSize + cubeSize * i;
                sample = screen.getSubimage(gemCenterX - 2, gemCenterY - 2, sampleSize, sampleSize);

                int redTotal = 0, greenTotal = 0, blueTotal = 0;
                for (int y = sample.getMinY(); y < sample.getHeight(); y++) {
                    for (int x = sample.getMinX(); x < sample.getWidth(); x++) {
                        int colorSample = sample.getRGB(x, y);
                        redTotal += colorSample >> 16 & 0xFF;
                        greenTotal += colorSample >> 8 & 0xFF;
                        blueTotal += colorSample & 0xFF;
                    }
                }
                averageColor = new Color(redTotal / sampleArea, greenTotal / sampleArea, blueTotal / sampleArea);
                gemColors[i][j] = lookupGemColor(averageColor.getRGB());
            }
        }

        return gemColors;
    }

    /**
     * Swap two gems on a board
     *
     * @param board The gem board
     * @param x1 Gem1 x
     * @param y1 Gem1 y
     * @param x2 Gem2 x
     * @param y2 Gem2 y
     * @return The gem board
     */
    public static int[][] swapGems(int[][] board, int x1, int y1, int x2, int y2) {
        int temp = board[y1][x1];
        board[y1][x1] = board[y2][x2];
        board[y2][x2] = temp;
        return board;
    }

    public static int scoreAll(int[][] board) {
        int x2, y2;
        int score = 0;
        int gem;
        boolean[][] countedBoard = new boolean[8][8];
        //The gemBuffer keeps track of gems we have counted in a given direction,
        //but don't know yet if they're to be counted!
        LinkedList<Point> gemBuffer = new LinkedList<Point>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gem = board[i][j];

                if (!countedBoard[i][j]) {
                    gemBuffer.clear();
                    gemBuffer.add(new Point(j, i));

                    for (x2 = j - 1; x2 >= 0 && board[i][x2] == gem && (!countedBoard[i][x2]); x2--) {
                        gemBuffer.add(new Point(x2, i));
                    }
                    //Score rightwards
                    for (x2 = j + 1; x2 < 8 && board[i][x2] == gem && (!countedBoard[i][x2]); x2++) {
                        gemBuffer.add(new Point(x2, i));
                    }

                    if (gemBuffer.size() >= 3) {
                        score += gemBuffer.size();
                        for (Point usedPoint : gemBuffer) {
                            countedBoard[usedPoint.y][usedPoint.x] = true;
                        }
                    }

                    gemBuffer.clear();
                    gemBuffer.add(new Point(j, i));
                    //Score upwards
                    for (y2 = i - 1; y2 >= 0 && board[y2][j] == gem && !countedBoard[y2][j]; y2--) {
                        gemBuffer.add(new Point(j, y2));
                    }
                    //Score downwards
                    for (y2 = i + 1; y2 < 8 && board[y2][j] == gem && !countedBoard[y2][j]; y2++) {
                        gemBuffer.add(new Point(j, y2));
                    }

                    if (gemBuffer.size() >= 3) {
                        score += gemBuffer.size();
                        for (Point usedPoint : gemBuffer) {
                            countedBoard[usedPoint.y][usedPoint.x] = true;
                        }
                    }
                }
            }
        }

        return score;
    }

    /**
     * Calculate (roughly) the distance between the colors of two gems
     *
     * @param intColor1 Colour of gem1
     * @param intColor2 Colour of gem2
     * @return distance
     */
    private final double colorDistance(int intColor1, int intColor2) {
        Color color1 = new Color(intColor1);
        Color color2 = new Color(intColor2);

        long rmean = (color1.getRed() + color2.getRed()) / 2;
        long r = color1.getRed() - color2.getRed();
        long g = color1.getGreen() - color2.getGreen();
        long b = color1.getBlue() - color2.getBlue();
        return Math.sqrt(
                (((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8)
        );
    }

    /**
     * Find the predefined gem colour that matches a sampled gem's colour
     *
     * @param gemColor The sampled gem colour
     * @return The predefined gem colour
     */
    private int lookupGemColor(int gemColor) {
        double minDist = 9999;
        int minIndex = 0;
        double cDist;
        for (int i = 0; i < 7; i++) {
            cDist = colorDistance(gemColor, idealGemColors[i]);
            if (cDist < minDist) {
                minDist = cDist;
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static void printBoard(int[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(Board.getColorName(board[i][j]).charAt(0) + "\t");
            }
            System.out.print("\n");
        }
    }
}
