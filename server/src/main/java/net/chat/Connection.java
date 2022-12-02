package net.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Connection extends Thread {

    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private String userName = "undefined";
    private boolean isConnected = false;
    private Logger logger;
    private final static List<Connection> connections = ChatServer.getConnections();

    public Connection(Socket socket, Logger logger) {
        try {
            this.logger = logger;
            inputStream = buildInputStream(socket);
            outputStream = buildOutputStream(socket);
            logger.log("Созданы, BufferedReader и PrintWriter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrintWriter buildOutputStream(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }

    public static BufferedReader buildInputStream(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().setName(userName);
            logger.log(
                    sendMessageToClient("Введите имя пользователя: ")
            );
            String notCheckedUserName = readClientMessage(inputStream);
            boolean nameIsValid = userNameValidation(notCheckedUserName);
            while (!nameIsValid) {
                logger.log(
                        sendMessageToClient(
                                "Имя не уникально или = exit, или менее 2х символов"
                        )
                );
                notCheckedUserName = readClientMessage(inputStream);
                nameIsValid = userNameValidation(notCheckedUserName);
            }
            userName = notCheckedUserName;

            Thread.currentThread().setName(userName);
            setTrueIsConnected();
            logger.log(
                    sendMessageToConnectedClients("[" + userName + "] вошел в чат")
            );
            String messageFromClient;
            while (true) {
                messageFromClient = readClientMessage(inputStream);
                if (messageFromClient.equals("exit")) {
                    logger.log("[Пользователь " + userName + " набрал команду exit]");
                    break;
                } else if (messageFromClient.equals("")) {
                    continue;
                }
                logger.log(
                        sendMessageToConnectedClients("[" + userName + "] " + messageFromClient)
                );
            }
            logger.log(
                    sendMessageToConnectedClients("[" + userName + "] покинул в чат")
            );
            closeConnection();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            connections.remove(this);
        } catch (IOException exception) {
            exception.printStackTrace();
            logger.log("[Ошибка закрытия inputStream или outputStream] " + exception);
        }
    }

    public static String readClientMessage(BufferedReader inputStream) throws IOException {
        try {
            return inputStream.readLine();
        } catch (IOException exception) {
            throw new RuntimeException("[Клиент оборвал соединение] \n" + exception);
        }
    }


    public boolean userNameValidation(String notCheckedUserName) {
        if (notCheckedUserName.length() < 2
                || notCheckedUserName.equalsIgnoreCase("exit")
                || notCheckedUserName.equalsIgnoreCase("unDEfined")) {
            return false;
        }
        String connectionName;
        synchronized (connections) {
            for (int i = 0; i < connections.size(); i++) {
                connectionName = connections.get(i).userName;
                if (notCheckedUserName.equalsIgnoreCase(connectionName)) {
                    return false;
                }
            }
            return true;
        }
    }

    public String sendMessageToClient(String message) {
        outputStream.println(message);
        return " [отправлено сообщение от: " + userName + "] "
                + message;

    }

    public String sendMessageToConnectedClients(String message) {
        synchronized (connections) {
            for (int i = 0; i < connections.size(); i++) {
                if (connections.get(i).isConnected()) {
                    connections.get(i).sendMessageToClient(message);

                }
            }
        }
        return " [отправлено всем подключенным клиентам] "
                + message;
    }


    public boolean isConnected() {
        return this.isConnected;
    }

    public void setTrueIsConnected() {
        isConnected = true;
    }
}
