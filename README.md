# Bejeweled Bot

Bejeweled bot is a Java application that plays PopCap's [Bejeweled Blitz](https://www.facebook.com/bejeweledblitz)
by taking screenshots of the screen, detecting gem positions based on colour and moving gems using the robot mouse API.

[Watch the video on YouTube](http://www.youtube.com/watch?v=MY0OTe0WJNM) to see it in action.

## How It Works

- A screenshot of the game board is taken with `gameScreenshot = robot.createScreenCapture(gameRectangle);`
- The colours of the gems are detected with `boardGems = board.getBoardGemColors(gameScreenshot);`
- The moves (gem swaps) to make are calculated with `LinkedList<Move> moves = Solver.solve(boardGems);`
- The moves are executed using the `java.awt.Robot` mouse API:
  * The mouse is moved with `robot.mouseMove`
  * The mouse is clicked with `robot.mousePress(InputEvent.BUTTON1_MASK);` and `robot.mouseRelease(InputEvent.BUTTON1_MASK);`
  * This is performed on both of the gems to be swapped, causing them to swap places.
- This process repeats until the game ends or there are no moves to make a number of times in a row.

## Issues

- The solver (that calculates the moves to make) could be better
- The supported gem colours are hardcoded but they could be automatically recognised
- Special gems (other than the standard seven colours) are not handled
- The screen position (top left) of the board is hard coded
- The game and the bot must be manually started

## License

This project is released under the MIT License - see the LICENSE file for details.