package test;

import org.junit.jupiter.api.Test;
import roomba.GameField;
import roomba.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GameFieldTest {
    //TODO
    @Test
    void testPlayerDirection(){
        GameField gameField = new GameField(null);
        Player player = new Player(gameField, null, 0);
    }
//    @Test
//    void testPlayerMovement() {
//        GameField gameField = new GameField(null);
//        Player player = new Player(gameField, null, 0);
//        assertTrue();
//    }
}

