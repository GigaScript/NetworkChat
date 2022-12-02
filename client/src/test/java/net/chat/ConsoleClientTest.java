package net.chat;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsoleClientTest {
    private final String testClientMessage = "test message";
    Socket socketMock = mock(Socket.class);
    ByteArrayInputStream inContent = new ByteArrayInputStream(testClientMessage.getBytes());
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    void isExit() {
        String userMessage = "Привет";
        assertFalse(ConsoleClient.isExit(userMessage));
        userMessage = "exit";
        assertTrue(ConsoleClient.isExit(userMessage));
        userMessage = "EXit";
        assertTrue(ConsoleClient.isExit(userMessage));
    }

    @Test
    void buildInputStream() throws IOException {
        when(socketMock.getInputStream()).thenReturn(inContent);
        assertNotNull(ConsoleClient.buildInputStream(socketMock));
    }

    @Test
    void buildOutputStream() throws IOException {
        when(socketMock.getOutputStream()).thenReturn(outContent);
        assertNotNull(ConsoleClient.buildOutputStream(socketMock));
    }

    @Test
    void sentMessageToServer() throws IOException {
        when(socketMock.getOutputStream()).thenReturn(outContent);
        PrintWriter outputStream = ConsoleClient.buildOutputStream(socketMock);
        System.setOut(new PrintStream(outContent));
        ConsoleClient.sentMessageToServer(testClientMessage, outputStream);
        byte[] bytesFromOutString = outContent.toByteArray();
        String textFromByteArray = new String(
                bytesFromOutString,
                0,
                testClientMessage.length(),
                StandardCharsets.UTF_8);
        assertEquals(textFromByteArray, testClientMessage);
    }

    @Test
    void readUserMessage() throws IOException {
        System.setIn(inContent);
        Scanner scanner = new Scanner(System.in);
        assertEquals(ConsoleClient.readUserMessage(scanner), testClientMessage);
        System.setIn(System.in);
    }

}