# Content Table
[Quick_Overview](#quick-overview)

[Roomba Game Code Documentation](#roomba-game-code-documentation)

[Source Code Qualität Anforderungen](#source-code-qualität-anforderungen)

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



# Source Code Qualität Anforderungen


## Einführung

Dieses Dokument legt die Qualitätsanforderungen für den Quellcode der "Roomba in trouble" Java-Anwendung fest. Ziel ist es, sicherzustellen, dass der Code lesbar, wartbar, erweiterbar und fehlerfrei ist. Es enthält Richtlinien und Empfehlungen zu Code-Stil, Code-Kommentierung, Testabdeckung, Dokumentation, Wartbarkeit, Erweiterbarkeit, Performance und Sicherheit.

## Code-Stil

Der Quellcode muss in Übereinstimmung mit den folgenden Code-Stil-Konventionen geschrieben werden:

- Einheitliche Namensgebung von Variablen und Methoden: Verwenden Sie CamelCase für Variablen und Methoden, und PascalCase für Klassen.
- Anordnung von Importanweisungen: Gruppieren Sie Importanweisungen nach Paketen und sortieren Sie sie alphabetisch.
- Einheitliche Einrückung und Formatierung: Verwenden Sie 4 Leerzeichen für die Einrückung und platzieren Sie geschweifte Klammern auf einer neuen Zeile.
- Verwendung von sprechenden Variablennamen und Kommentaren zur Erklärung von komplexen Logikabschnitten.
- Verwendung von Leerzeichen zur Verbesserung der Lesbarkeit des Codes.

## Code-Kommentierung

Der Code muss ausreichend kommentiert werden, um seine Funktion und seinen Zweck zu erklären. Die Kommentare sollten die folgenden Aspekte abdecken:

- Erklärung der Funktion und des Zwecks von Variablen, Methoden und Klassen.
- Erklärung von komplexen Logikabschnitten.
- Dokumentation von Änderungen am Code, einschließlich Autor und Datum der Änderung.

## Code-Testabdeckung


- Handlich die wichtigsten Schnittstellen getestet

## Code-Dokumentation

Der Quellcode muss ausreichend dokumentiert werden, um seine Funktion und seinen Zweck zu erklären. Die Dokumentation sollte die folgenden Aspekte abdecken:

- Erklärung der Funktion und des Zwecks von Variablen, Methoden und Klassen.
- Erklärung von komplexen Logikabschnitten.
- Dokumentation von Änderungen am Code, einschließlich Autor und Datum der Änderung.

## Code-Wartbarkeit

Der Quellcode muss wartbar sein, um sicherzustellen, dass er leicht erweitert oder geändert werden kann. Dazu sollten folgende Maßnahmen ergriffen werden:

- Verwendung von sprechenden Variablennamen und Kommentaren zur Erklärung von komplexen Logikabschnitten.
- Verwendung von Leerzeichen zur Verbesserung der Lesbarkeit des Codes.
- Konsistente Einrückung und Formatierung des Codes.
- Verwendung von Kommentaren zur Dokumentation des Codes.
- Modularer und gut strukturierter Code, um Wartbarkeit und Verständlichkeit zu erleichtern.

## Code-Erweiterbarkeit

Der Quellcode muss erweiterbar sein, um sicherzustellen, dass er leicht um neue Funktionen erweitert werden kann. Dazu sollten folgende Maßnahmen ergriffen werden:

- Verwendung von sprechenden Variablennamen und Kommentaren zur Erklärung von komplexen Logikabschnitten.
- Verwendung von Leerzeichen zur Verbesserung der Lesbarkeit des Codes.
- Konsistente Einrückung und Formatierung des Codes.
- Verwendung von Kommentaren zur Dokumentation des Codes.
- Anwendung von Design-Patterns und Architekturprinzipien, die die Erweiterbarkeit unterstützen, wie z.B. MVC.

## Code-Performance

Der Quellcode muss gut performen, um sicherzustellen, dass das System reaktionsschnell und benutzerfreundlich ist. Dazu sollten folgende Maßnahmen ergriffen werden:

- Verwendung von effizienten Algorithmen und Datenstrukturen.
- Minimierung der Anzahl an teuren Operationen und Ressourcenverbrauch.
- Verwendung von Caching und Lazy-Loading, wo es angemessen ist.

## Fazit

Die Einhaltung dieser Anforderungen an die Source Code Qualität stellt sicher, dass der Quellcode der "Roomba in trouble" Java-Anwendung lesbar, wartbar, erweiterbar, fehlerfrei, performant und sicher ist. Die Beachtung dieser Best Practices hilft dabei, eine hohe Codequalität zu gewährleisten und das Risiko von Fehlern und Sicherheitslücken zu minimieren.
