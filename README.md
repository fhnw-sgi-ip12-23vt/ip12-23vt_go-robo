# Quick Overview
The `RoombaInTrouble.java` class initializes the Physical MVC, which handles RFID card inputs. `GameField` contains the entire game logic. It loads images once at the start and uses them throughout the application's lifetime. The game is displayed using the Processing draw method. When one level is completed, the `LevelManager` loads the next level.

## Starter
The starter class is `RoombaInTrouble.java`.

## app.properties
This file contains game properties that can be changed. If an invalid property is set, the game will run with the defaults.

## Level
Under `resources/files/level`, the level CSV files can be modified or new ones added.
- `0`: stands for air
- `1`: is the goal
- `2`: walls
- `3`: obstacles

## ImageLoader
If, for some reason, the images are not loading, the only path change needed is in the `ImageLoader` class.
