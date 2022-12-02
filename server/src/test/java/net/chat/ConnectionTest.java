package net.chat;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConnectionTest {
    final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());
    private final String testClientMessage = "Ура я смог написать тест";
    @InjectMocks
    private static final Logger LOGGER = new Logger("./log.txt");
    ByteArrayInputStream inContent = new ByteArrayInputStream(testClientMessage.getBytes());
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    Socket socketMock = mock(Socket.class);

    ConnectionTest() {
        Connection connectionMock = mock(Connection.class);
        connectionMock.setTrueIsConnected();
        connections.add(connectionMock);
        connections.add(connectionMock);
        connections.add(connectionMock);
    }

    @Test
    void buildOutputStream() throws IOException {
        when(socketMock.getOutputStream()).thenReturn(outContent);
        assertNotNull(Connection.buildOutputStream(socketMock));
    }

    @Test
    void buildInputStream() throws IOException {
        when(socketMock.getInputStream()).thenReturn(inContent);
        assertNotNull(Connection.buildInputStream(socketMock));
    }

    @Test
    void closeConnection() throws IOException {
        when(socketMock.getInputStream()).thenReturn(inContent);
        BufferedReader bufferedReader = Connection.buildInputStream(socketMock);
        assertNotNull(bufferedReader);
        assertTrue(bufferedReader.ready());
        bufferedReader.close();
        assertThrows(IOException.class, bufferedReader::ready);

        when(socketMock.getOutputStream()).thenReturn(outContent);
        PrintWriterMock printWriter = new PrintWriterMock(
                Connection.buildOutputStream(socketMock)
        );
        assertNotNull(printWriter);
        assertTrue(printWriter.isOpen());
        printWriter.close();
        assertFalse(printWriter.isOpen());
    }

    @Test
    void readClientMessage() throws IOException {
        BufferedReader bufferedReaderMock = mock(BufferedReader.class);
        when(bufferedReaderMock.readLine()).thenReturn(testClientMessage);
        assertEquals(Connection.readClientMessage(bufferedReaderMock), testClientMessage);
    }

    @Test
    void userNameValidation() {
        assertFalse(checkCorrectlyChatUserName("a"));
        assertFalse(checkCorrectlyChatUserName("eXiT"));
        assertFalse(checkCorrectlyChatUserName("exit"));
        assertFalse(checkCorrectlyChatUserName("undefined"));
        assertFalse(checkCorrectlyChatUserName("unDEfined"));
        assertTrue(checkCorrectlyChatUserName("aa"));
        assertTrue(checkCorrectlyChatUserName("DEfined"));
        assertTrue(checkCorrectlyChatUserName("tixe2"));
    }

    boolean checkCorrectlyChatUserName(String name) {
        String connectionName;
        if (name.length() < 2
                || name.equalsIgnoreCase("exit")
                || name.equalsIgnoreCase("unDEfined")) {
            return false;
        }
        for (int i = 0; i < connections.size(); i++) {
            connectionName = connections.get(i).getName();
            if (name.equalsIgnoreCase(connectionName)) {
                return false;
            }
        }
        return true;
    }

    @Test
    void sendMessageToClient() {
        Connection connectionMock = mock(Connection.class);
        when(connectionMock.sendMessageToClient(testClientMessage)).thenReturn(testClientMessage);
        assertEquals(connectionMock.sendMessageToClient(testClientMessage), testClientMessage);
    }

    @Test
    void sendMessageToConnectedClients() throws IOException {
        when(socketMock.getOutputStream()).thenReturn(outContent);
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).isConnected()) {
                assertEquals(testClientMessage, connections.get(i).sendMessageToClient(testClientMessage));
            }
        }
    }
}