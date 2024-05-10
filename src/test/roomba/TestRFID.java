//package test.roomba;
//
//
//import ch.qos.logback.classic.Level;
//import com.pi4j.context.Context;
//import com.pi4j.plugin.mock.provider.spi.MockSpi;
//import org.slf4j.LoggerFactory;
//import processing.core.PApplet;
//import roomba.controller.LedController;
//import roomba.controller.PhysicalController;
//import roomba.model.LedModel;
//import roomba.model.PhysicalModel;
//import roomba.util.Pi4JContext;
//import roomba.view.GameField;
//import roomba.view.PhysicalLed;
//import roomba.view.PhysicalScanner;
//import org.junit.Test;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertNull;
//
//
//public class TestRFID {
//    @Test
//    public void TestQueue() throws InterruptedException {
//        var mockSpiLogger = LoggerFactory.getLogger(MockSpi.class);
//        var logbackLogger = (ch.qos.logback.classic.Logger) mockSpiLogger;
//        logbackLogger.setLevel(Level.WARN);
//
//        Context context = Pi4JContext.createContext();
//        PhysicalModel model = new PhysicalModel();
//        PhysicalController controller = new PhysicalController(model);
//        PhysicalScanner pui = new PhysicalScanner(controller, context);
//        LedModel lm = new LedModel();
//        LedController lc = new LedController(lm);
//        PhysicalLed pl = new PhysicalLed(lc, context);
//
//
//        GameField gameField = new GameField(pui, pl);
//        PApplet.runSketch(new String[] {"Game Field"}, gameField);
//        gameField.setup();
//
//       var inputT = gameField.getLastInputs();
//       var cX = gameField.player.changeY;
//        String id ="BA279E16";
//        pui.getController().enqueue(id);
//        assertNull(pui.getController().getQueue().poll());
//
//        assertEquals(inputT.get(0), gameField.getLastInputs().get(0));
//
//        assertNotEquals(cX, gameField.player.changeY);
//
//
//    }
//}
//
