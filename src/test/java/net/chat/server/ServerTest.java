package net.chat.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServerTest {
    private final InetAddress serverIpName = InetAddress.getByName("localhost");
    private final InetAddress serverIpAddress = InetAddress.getByName("127.0.0.1");

    ServerTest() throws IOException {

    }

    @Test
    void createSocketByServerName() throws IOException {
        final int testPort = 2022;
        assertNotNull(Server.createServerSocket(serverIpName, testPort));
    }

    @Test
    void createSocketByIpAddress() throws IOException {
        final int testPort = 2022;
        assertNotNull(Server.createServerSocket(serverIpName, testPort));
    }

    @Test
    public void serverSocketPortCorrectly() throws IOException {
        final int testPort = 2030;
        ServerSocket testServerSocket = Server.createServerSocket(serverIpName, testPort);
        assertEquals(testServerSocket.getLocalPort(), testPort);
    }

    @Test
    void acceptClientConnection() throws IOException {
        ServerSocket mockServerSocket = mock(ServerSocket.class);
        when(mockServerSocket.accept()).thenReturn(new Socket());
        assertNotNull(Server.acceptClientConnection(mockServerSocket));
    }

    @Test
    void MessageAfterConnect() {
        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getInetAddress()).thenReturn(serverIpAddress);
        assertEquals(Server.messageAfterConnect(mockSocket), "Подключен новый участник 127.0.0.1");
    }
}