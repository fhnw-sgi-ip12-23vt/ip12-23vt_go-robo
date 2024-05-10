module RoboGo {
    requires com.pi4j;
    requires java.logging;
    requires core;
    requires com.pi4j.library.pigpio;
    requires com.pi4j.plugin.linuxfs;
    requires com.pi4j.plugin.mock;
    requires com.pi4j.plugin.pigpio;
    requires com.pi4j.plugin.raspberrypi;
    requires com.pi4j.crowpi;
    requires org.slf4j;
    requires java.desktop;
    requires ch.qos.logback.classic;
    requires junit;
    requires org.mockito;
    // Export the test package to the junit module
}