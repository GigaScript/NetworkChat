package net.chat;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageReceiverTest {
    String testMessage = "Ура я смог написать тест";
    ByteArrayInputStream inContent = new ByteArrayInputStream(testMessage.getBytes());
    Socket socketMock = mock(Socket.class);
    @Test
    void receiveMessageFromServer() throws IOException {
        when(socketMock.getInputStream()).thenReturn(inContent);
        BufferedReader bufferedReader = ConsoleClient.buildInputStream(socketMock);
        assertNotNull(bufferedReader);
        assertTrue(bufferedReader.ready());
        assertEquals(bufferedReader.readLine(),testMessage);
    }
}