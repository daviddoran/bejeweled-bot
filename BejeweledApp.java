package bejeweled;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class BejeweledApp {
    /**
     * @param args
     * @throws AWTException
     */
    public static void main(String[] args) {
        Robot robot;

        //the size of a square on the board
        int cubeSize = 40;
        //the size of the sample to determine a gem's colour
        int sampleSize = 4;

        //the position of the top left of the board (in screen coordinates)
        int gameLeft = 507;
        int gameTop  = 345;

        //game length (in milliseconds)
        long gameLength = 63 * 1000;

        //the maximum number of times (in a row) there can be no moves to make
        long noMoveCutoff = 5;

        //the maximum number of gems to move at the same time
        int maxMovesPerRound = 10;

        //save screenshots to disk
        boolean saveScreenshots = false;

        //the extent of the game board
        Rectangle gameRectangle = new Rectangle(gameLeft, gameTop, cubeSize * 8, cubeSize * 8);

        //the robot will move the mouse and perform clicks
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            return;
        }

        final GameKeyListener frame = new GameKeyListener("Bejeweled AI Player");

        JOptionPane.showConfirmDialog(frame, "Press yes to start...");

        try {
            Thread.sleep(3500);
        } catch (Exception e) {}

        BufferedImage gameScreenshot;
        Board board = new Board(cubeSize, sampleSize);
        Move nextMove;

        //the gem colours are stored in a 2D array
        int[][] boardGems;

        //counts the number of times (in a row) no move has been available
        int noMoveCount = 0;
        //the number of screenshots saved to disk
        int screenshotCount = 0;

        long startTime = System.currentTimeMillis();
        long endTime   = startTime + gameLength;


        while (System.currentTimeMillis() < endTime) {
            if (noMoveCount > (noMoveCutoff - 1)) {
                System.out.println(noMoveCutoff + " no-move rounds in a row...exiting!");
                System.exit(0);
                return;
            }

            //take a screenshot of the screen
            gameScreenshot = robot.createScreenCapture(gameRectangle);

            if (saveScreenshots) {
                File outputfile = new File("screenshot" + screenshotCount + ".jpg");
                try {
                    ImageIO.write(gameScreenshot, "jpg", outputfile);
                    screenshotCount ++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //figure out the positions of the gems on the board
            boardGems = board.getBoardGemColors(gameScreenshot);

            try {
                //calculate the best moves to make
                LinkedList<Move> moves = Solver.solve(boardGems);

                if (moves.size() > 0) {
                    //Reset no-move counter
                    noMoveCount = 0;

                    int movesToMake = Math.min(moves.size(), maxMovesPerRound);
                    for (int i = 0; i < movesToMake; i++) {
                        nextMove = moves.get(i);

                        robot.mouseMove(gameLeft + cubeSize / 2 + cubeSize * nextMove.x1, gameTop + cubeSize / 2 + cubeSize * nextMove.y1);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);

                        robot.mouseMove(gameLeft + cubeSize / 2 + cubeSize * nextMove.x2, gameTop + cubeSize / 2 + cubeSize * nextMove.y2);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    }
                }
            } catch (NoMoveException e) {
                System.out.println("ERROR: No move.");
                noMoveCount++;
                try {
                    Thread.sleep(600);
                } catch (Exception e2) {}
            }

            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.toFront();
                    frame.repaint();
                }
            });

            try {
                //Pause a bit to let the game catch up!
                Thread.sleep(300);
            } catch (Exception e) {
            }
        }

        System.exit(0);
    }
}
