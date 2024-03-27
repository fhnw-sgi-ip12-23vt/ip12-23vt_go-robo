//package test;
//
//import ch.qos.logback.classic.Level;
//import com.pi4j.plugin.mock.provider.spi.MockSpi;
//import org.slf4j.LoggerFactory;
//import roomba.GameField;
//import roomba.Player;
//import roomba.controller.PhysicalController;
//import roomba.model.PhysicalModel;
//import roomba.util.Pi4JContext;
//import roomba.view.PhysicalScanner;
//
//
//
//public class GameFieldTest {
//    //TODO
//
//    public static void main(String[] args) {
//        testQueue();
//    }
//   public static void testQueue(){
//       var mockSpiLogger = LoggerFactory.getLogger(MockSpi.class);
//       var logbackLogger = (ch.qos.logback.classic.Logger) mockSpiLogger;
//       logbackLogger.setLevel(Level.WARN);
//        String card = "test";
//        PhysicalModel model = new PhysicalModel();
//
//        PhysicalController controller = new PhysicalController(model);
//        PhysicalScanner pui = new PhysicalScanner(controller, Pi4JContext.createContext());
//
//        pui.controller.enqueue(card);
//
//
//
//
//        pui.controller.subscribeToQueueChanges((oldValue, newValue) -> {
//            System.out.println("OK" + newValue + " .... "+ card);
//        });
//   }
//}
//
