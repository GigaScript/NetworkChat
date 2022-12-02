package net.chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer {
    private final InetAddress serverIpAddress;
    private final int serverPort;
    private final Logger logger;
    private ServerSocket serverSocket;
    static Socket socket;
    static final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());


    public ChatServer(final InetAddress serverIpAddress, final int serverPort, final Logger logger) {
        this.logger = logger;
        this.serverIpAddress = serverIpAddress;
        this.serverPort = serverPort;

    }

    public void startServer() throws IOException {
        try {
            serverSocket = buildServerSocket(serverPort, serverIpAddress);
            String startMessage = "[Сервер запущен]: SERVER_IP_ADDRESS=" + serverIpAddress + " SERVER_PORT=" + serverPort;
            logger.log(startMessage);
            System.out.println(startMessage);
            while (true) {
                socket = waitNewConnection(serverSocket);
                prepareNewConnection(connections);
            }
        } catch (IOException exception) {
            logger.log("[Сервер остановлен с ошибкой]: " + exception);
            exception.printStackTrace();
        } finally {
            closeServer();
            logger.log("[Сервер остановлен, serverSocket и socket- закрыты]");
        }
    }

    public void prepareNewConnection(List<Connection> connections) {
        final Connection connection = getConnection();
        connections.add(connection);
        connection.start();
    }

    public void closeServer() throws IOException {
        this.serverSocket.close();
        socket.close();
    }

    public Connection getConnection() {
        return new Connection(socket, this.logger);
    }

    public static Socket waitNewConnection(ServerSocket serverSocket) throws IOException {
        return serverSocket.accept();
    }

    public static List<Connection> getConnections() {
        return connections;
    }

    public static ServerSocket buildServerSocket(int serverPort, InetAddress serverIpAddress) throws IOException {
        return new ServerSocket(serverPort, 50, serverIpAddress);
    }
}
