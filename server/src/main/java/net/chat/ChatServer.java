package net.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer {
    private static final ServerSetting SERVER_SETTING = ServerSetting.getInstance();
    private static final InetAddress SERVER_IP_ADDRESS = SERVER_SETTING.serverIpAddress();
    private static final int SERVER_PORT = SERVER_SETTING.getServerPort();
    private static final Logger LOGGER = new Logger(SERVER_SETTING.getLogPath());
    private static ServerSocket serverSocket;
    static Socket socket;
    static final List<Connection> connections = Collections.synchronizedList(new ArrayList<>());

    private ChatServer() {

    }

    public static void main(String[] args) throws IOException {
        try {
            serverSocket = new ServerSocket(SERVER_PORT, 50, SERVER_IP_ADDRESS);
            LOGGER.log("Сервер запущен: SERVER_IP_ADDRESS=" + SERVER_IP_ADDRESS + " SERVER_PORT=" + SERVER_PORT);
            while (true) {
                socket = serverSocket.accept();
                final Connection connection = new Connection(socket);
                connections.add(connection);
                connection.start();
            }
        } catch (IOException exception) {
            LOGGER.log("Сервер остановлен с ошибкой: " + exception);
            exception.printStackTrace();
        } finally {
            serverSocket.close();
            socket.close();
            LOGGER.log("Сервер остановлен, serverSocket и socket- закрыты");
        }
    }
    public static Logger getLogger() {
        return LOGGER;
    }
    public static List<Connection> getConnections() {
        return connections;
    }

}
