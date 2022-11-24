package net.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThreadController implements Runnable {

    public static PrintWriter createMessageSender;
    private final Socket clientSocket;

    public ClientThreadController(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        PrintWriter messageSender = null;
        BufferedReader messageReceiver = null;
        try {
            messageSender = createMessageSender(clientSocket);
            messageReceiver = createMessageReceiver(clientSocket);
            String clientMessage = isHaveNewMessage(messageReceiver);
            while (clientMessage != null) {
                printMessageFromClient(clientMessage);
                sentMessageToClients(messageSender, clientMessage);
                clientMessage = isHaveNewMessage(messageReceiver);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeOutputStream(messageSender);
                closeInputStream(messageReceiver);
                closeClientSocket( clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void sentMessageToClients(PrintWriter messageSender, String clientMessage) {
        messageSender.println(clientMessage);
    }

    public static void printMessageFromClient(String clientMessage) {
        System.out.printf(" Сообщение от Клиента: %s\n", clientMessage);
    }

    public static String isHaveNewMessage(BufferedReader messageReceiver) throws IOException {
        return messageReceiver.readLine();
    }


    public static PrintWriter createMessageSender(Socket connectedSocket) throws IOException {
        return new PrintWriter(
                connectedSocket.getOutputStream(), true);
    }

    public static BufferedReader createMessageReceiver(Socket connectedSocket) throws IOException {
        return new BufferedReader(new InputStreamReader(
                connectedSocket.getInputStream())
        );
    }

    public static void closeOutputStream(PrintWriter sentToAllClients) {
        if (sentToAllClients != null) {
            sentToAllClients.close();
        }
    }

    public static void closeInputStream(BufferedReader receiveFromClient) throws IOException {
        if (receiveFromClient != null) {
            receiveFromClient.close();

        }
    }
    static void closeClientSocket(Socket clientSocket) throws IOException {
        if (clientSocket != null) {
            clientSocket.close();
        }
    }
}
