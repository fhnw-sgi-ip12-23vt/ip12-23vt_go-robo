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

# further explanation:
# Roomba Game Code Documentation

## AnimatedSprite Class

### Description
The `AnimatedSprite` class extends the `Sprite` class and represents a sprite with animation capabilities. It is used for animated game elements.

### Constructors
- `AnimatedSprite(PApplet pApplet, PImage img, float scale)`: Initializes an animated sprite with the given PApplet, image, and scale.

### Methods
- `updateAnimation()`: Updates the animation frame based on the movement direction.
- `selectDirection()`: Selects the current movement direction.
- `selectCurrentImages()`: Selects the current set of images based on the direction.
- `advanceToNextImage()`: Advances to the next frame in the animation.

## CollisionHandler Class

### Description
The `CollisionHandler` class handles collision detection and resolution between sprites in the game.

### Methods
- `resolveObstaclesCollisions(Sprite s, List<Sprite> walls)`: Resolves collisions between a sprite and a list of wall sprites.
- `checkCollision(Sprite s1, Sprite s2)`: Checks if two sprites are colliding.
- `checkCollisionList(Sprite s, List<Sprite> list)`: Checks collision with a list of sprites and returns the colliding ones.

## Constants Class

### Description
The `Constants` class holds static constants and configurations for the game.

### Fields
- `MOVE_SPEED`: The movement speed of game elements.
- `SPRITE_SCALE`: The scale factor for sprites.
- `SPRITE_SIZE`: The size of sprites.
- `HEIGHT`: The height of the game field.
- `WIDTH`: The width of the game field.
- `NEUTRAL_FACING`, `RIGHT_FACING`, `LEFT_FACING`, `UP_FACING`, `DOWN_FACING`: Direction constants.
- `FULLSCREEN`: Boolean indicating whether the game runs in fullscreen mode.

### Methods
- `initConfigs()`: Initializes constants and configurations from an external properties file.

## GameField Class

### Description
The `GameField` class represents the main game field where the Roomba moves. It handles game logic, drawing, and input.

### Fields
- `pui`: The physical scanner.
- `levelManager`: Manages game levels.
- `collisionHandler`: Handles collisions between sprites.
- `nextLevel`: Boolean indicating whether the next level is reached.
- `obstacles`: List of obstacle sprites.
- `goal`: List of goal sprites.
- `wall`, `ball`, `toy`, `pillow`, `plushie`, `plant1`, `plant2`, `computer`, `paper`, `chargingStation`: Images for game elements.
- `difficulty`: Current game difficulty.
- `player`: The player sprite.
- `view_x`, `view_y`: View coordinates.
- `backgroundImage`: Background image.
- `init`: Boolean indicating whether the game is initialized.
- `winCondition`: Boolean indicating whether the win condition is met.

### Methods
- `settings()`: Configures the game window settings.
- `draw()`: Draws the game field.
- `updateAll()`: Updates animation and handles collisions.
- `displayAll()`: Displays all game elements on the screen.
- `collectGoal()`: Handles goal collection and win conditions.
- `setup()`: Sets up the initial game state.
- `loadImages()`: Loads images for game elements.
- `createPlatforms(String filename)`: Creates platforms based on the given level file.
- `PInputLogic()`: Handles physical input logic from the scanner.

## LevelManager Class

### Description
The `LevelManager` class manages game levels, including loading level files and creating platforms.

### Fields
- `levelName`: The name of the current level.
- `difficulty`: The current game difficulty.

### Methods
- `getLevelName()`: Returns the name of the current level.
- `getDifficulty()`: Returns the current game difficulty.
- `setDifficulty(int difficulty)`: Sets the game difficulty.
- `getNextLevel()`: Gets the next level file based on difficulty.
- `createPlatforms(GameField gameField, String filename)`: Creates platforms in the game field based on the level file.

## ImageLoader Class

### Description
The `ImageLoader` class loads images for the game.

### Methods
- `loadImage(PApplet applet, String filename)`: Loads an image using the provided PApplet and filename.
- `getImagePath(String filename)`: Gets the full path of the image file.

## Goal Class

### Description
The `Goal` class represents a goal sprite in the game.

### Constructors
- `Goal(PApplet pApplet, PImage img, float scale)`: Initializes a goal sprite with the given PApplet, image, and scale.

## PhysicalScanner Class

### Description
The `PhysicalScanner` class extends the `PuiBase` class and is responsible for handling RFID card inputs. It initializes the `SimpleRFID` component and sets up bindings between the RFID scanner and the `PhysicalController` to enqueue scanned serial numbers.

### Constructors
- `PhysicalScanner(PhysicalController controller, Context pi4J)`: Initializes the `PhysicalScanner` with a provided `PhysicalController` and a Pi4J context.

### Fields
- `rfid`: An instance of `SimpleRFID` for RFID scanning.
- `controller`: The associated `PhysicalController` instance.

### Methods
- `initializeParts()`: Initializes the `SimpleRFID` component.
- `setupUiToActionBindings(PhysicalController controller)`: Sets up bindings between the RFID scanner and the `PhysicalController` for enqueuing serial numbers.

## PhysicalModel Class

### Description
The `PhysicalModel` class represents the physical model of the Roomba game. It uses the `ObservableValue` class to maintain a queue of RFID card inputs.

### Fields
- `inputQueue`: An observable queue of RFID card inputs.

## PhysicalController Class

### Description
The `PhysicalController` class extends the `ControllerBase` class and acts as the controller for the physical model. It provides methods to subscribe to queue changes, enqueue RFID card inputs, and dequeue items from the input queue.

### Constructors
- `PhysicalController(PhysicalModel model)`: Initializes the `PhysicalController` with a provided `PhysicalModel`.

### Methods
- `subscribeToQueueChanges(ValueChangeListener<Queue<String>> listener)`: Subscribes to changes in the RFID input queue.
- `enqueue(String item)`: Enqueues an RFID card input into the model's input queue.
- `dequeue()`: Dequeues an item from the input queue.

# SimpleRFID Class

## Description
The `SimpleRFID` class encapsulates RFID card detection functionality using the `RfidComponent` from the Pi4J library. It provides a simplified interface for handling RFID card scans asynchronously.

## Constructors
- `SimpleRFID(Context pi4j)`: Initializes the `SimpleRFID` with a Pi4J context. The RFID component is initialized, and a single-threaded executor is set up to handle card detection events.

## Fields
- `executor`: An `ExecutorService` for handling asynchronous card detection events.
- `rfid`: An instance of `RfidComponent` for interacting with the RFID component.
- `onScan`: A `Consumer<String>` representing the callback function to be executed when a card is scanned.
- `logger`: A `Logger` instance for logging card detection events.

## Methods
- `onScan(Consumer<String> scanCallback)`: Sets the callback function to be executed when a card is scanned.

### Description
Sets the callback function to be executed when a card is scanned.

### Parameters
- `scanCallback`: A `Consumer<String>` representing the callback function. The function takes a `String` parameter representing the serial number of the scanned card.

