# Bejeweled Bot

Bejeweled bot is a Java application that plays PopCap's [Bejeweled Blitz](https://www.facebook.com/bejeweledblitz)
by taking screenshots of the screen, detecting gem positions based on colour and moving gems using the robot mouse API.

[Watch the video on YouTube](http://www.youtube.com/watch?v=MY0OTe0WJNM) to see it in action.

## Issues

- The solver (that calculates the moves to make) could be better
- The supported gem colours are hardcoded but they could be automatically recognised
- Special gems (other than the standard seven colours) are not handled
- The screen position (top left) of the board is hard coded
- The game and the bot must be manually started

## License

This project is released under the MIT License - see the LICENSE file for details.