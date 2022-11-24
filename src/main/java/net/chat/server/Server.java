package net.chat.server;

import java.io.IOException;
import java.net.*;

public class Server {
    private final InetAddress serverIp;
    private final int serverPort;
    ServerSocket serverSocket = null;

    public Server(InetAddress serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        runServer();
    }

    private void runServer() {
        try {
            serverSocket = createServerSocket(serverIp, serverPort);
            ClientThreadController newChatSocket;
            while (true) {
                Socket acceptedClient = acceptClientConnection(serverSocket);
                System.out.println(messageAfterConnect(acceptedClient));
                newChatSocket = new ClientThreadController(acceptedClient);
                new Thread(newChatSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static ServerSocket createServerSocket(InetAddress serverIp, int serverPort) throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverPort, 50, serverIp);
        serverSocket.setReuseAddress(true);
        return serverSocket;
    }

    static Socket acceptClientConnection(ServerSocket serverSocket) throws IOException {
        return serverSocket.accept();
    }

    static String messageAfterConnect(Socket connectedSocket) {
        String message = "Подключен новый участник "
                + connectedSocket.getInetAddress()
                .getHostAddress();
        return message;
    }


}
