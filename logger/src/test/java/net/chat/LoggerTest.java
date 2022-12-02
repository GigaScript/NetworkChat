package net.chat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {
    Logger logger = new Logger("./log.txt");

    @Test
    void getInstance() {
        assertNotNull(logger);
    }

    @Test
    void log() {
        Logger logger = Logger.getInstance();
        assertTrue(logger.log("Тест"));
    }
}