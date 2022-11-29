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
    private final static Logger LOGGER = ChatServer.getLogger();
    private final static List<Connection> connections = ChatServer.getConnections();

    public Connection(Socket socket) {
        try {
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintWriter(socket.getOutputStream(), true);
            LOGGER.log("Созданы, BufferedReader и PrintWriter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String notCheckedUserName = chooseUserName();
            boolean nameIsValid = userNameValidation(notCheckedUserName);
            while (!nameIsValid) {
                LOGGER.log("Попытка авторизации с невалидным именем");
                sendMessageToClient("Имя не уникально или = exit, или менее 2х символов");
                notCheckedUserName = chooseUserName();
                nameIsValid = userNameValidation(notCheckedUserName);
            }
            this.userName = notCheckedUserName;
            isConnected = true;

            sendMessageToAllClient("[" + userName + "] вошел в чат");
            String messageFromClient;
            while (true) {
                messageFromClient = readClientMessage();
                if (messageFromClient.equals("exit")) {
                    LOGGER.log("Пользователь " + userName + " набрал команду exit");
                    break;
                } else if (messageFromClient.equals("")) {
                    continue;
                }
                sendMessageToAllClient("[" + userName + "] " + messageFromClient);
            }
            closeConnection();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        sendMessageToAllClient("[" + userName + "] покинул в чат");
        closeAll();
        connections.remove(this);
    }

    private String readClientMessage() throws IOException {
        try {
            return inputStream.readLine();
        } catch (IOException exception) {
            throw new RuntimeException("Клиент оборвал соединение \n" + exception);
        }

    }

    private String chooseUserName() throws IOException {
        sendMessageToClient("Введите имя пользователя: ");
        return readClientMessage();
    }

    private boolean userNameValidation(String notCheckedUserName) {
        if (notCheckedUserName.length() < 2 || notCheckedUserName.equals("exit".toLowerCase())) {
            return false;
        }
        String connectionName;
        synchronized (connections) {
            for (int i = 0; i < connections.size(); i++) {
                connectionName = connections.get(i).getUserName();
                if (notCheckedUserName.equals(connectionName)) {
                    return false;
                }
            }
            return true;
        }
    }

    private void closeAll() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            LOGGER.log("Ошибка закрытия inputStream или outputStream " + exception);
        }
    }

    private void sendMessageToClient(String message) {
        outputStream.println(
                LOGGER.logWhitReturn(
                        "",
                        message)
        );
    }

    private void sendMessageToAllClient(String message) {
        LOGGER.log("message" + " отправлено всем подключенным клиентам");
        synchronized (connections) {
            for (int i = 0; i < connections.size(); i++) {
                if (connections.get(i).isConnected) {
                    connections.get(i).sendMessageToClient(message);
                }
            }
        }
    }

    private String getUserName() {
        return userName;
    }
}
