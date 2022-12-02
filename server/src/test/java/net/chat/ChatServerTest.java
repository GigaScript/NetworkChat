package net.chat;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatServerTest {
    final InetAddress SERVER_IP_ADDRESS = InetAddress.getByName("localhost");
    final int SERVER_PORT = 2022;
    final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());
    Logger logger = new Logger("./log.txt");

    ChatServerTest() throws IOException {
    }

    @Test
    void buildServerSocket() throws IOException {
        ServerSocket serverSocket = ChatServer.buildServerSocket(SERVER_PORT, SERVER_IP_ADDRESS);
        assertNotNull(serverSocket);
        assertEquals(2022, serverSocket.getLocalPort());
        assertEquals(SERVER_IP_ADDRESS, serverSocket.getInetAddress());
    }

    @Test
    void waitNewConnection() throws IOException {
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        when(mockServerSocket.accept()).thenReturn(new Socket());
        assertNotNull(ChatServer.waitNewConnection(mockServerSocket));
    }

    @Test
    void getConnection() {
        Connection mockConnection = mock(Connection.class);
        assertNotNull(mockConnection);
        assertFalse(mockConnection.isInterrupted());
    }

    @Test
    void prepareNewConnection() {
        Connection mockConnection = mock(Connection.class);
        connections.add(mockConnection);
        assertTrue(connections.contains(mockConnection));
    }

    @Test
    void closeServer() throws IOException {
        ServerSocket serverSocket = ChatServer.buildServerSocket(9999, SERVER_IP_ADDRESS);
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        when(mockServerSocket.accept()).thenReturn(new Socket());
        Socket socket = mockServerSocket.accept();
        assertFalse(serverSocket.isClosed());
        assertFalse(socket.isClosed());
        serverSocket.close();
        socket.close();
        assertTrue(serverSocket.isClosed());
        assertTrue(socket.isClosed());
    }

    @Test
    void getLogger() {
        Logger logger = new Logger("./log.txt");
        assertNotNull(logger);
        assertEquals(logger.logWhitReturn("Ура", "вроде получилось"), "вроде получилось");
    }

    @Test
    void getConnections() {
        List<Connection> connectionsTest = ChatServer.getConnections();
        Connection connection = mock(Connection.class);
        assertEquals(connectionsTest.size(),0);
        connectionsTest.add(connection);
        assertEquals(connectionsTest.size(),1);
        assertEquals(connectionsTest.get(0),connection);
    }


}