package net.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class ConsoleClient {
    private final InetAddress serverIpAddress;
    private final int serverPort;
    public static Logger logger;
    private final Scanner scanner = new Scanner(System.in);
    private PrintWriter outputStream;

    public ConsoleClient(final InetAddress serverIpAddress, final int serverPort, final Logger logger) {
        this.logger = logger;
        this.serverIpAddress = serverIpAddress;
        this.serverPort = serverPort;
    }


    public void startClient() {
        try (Socket socket = new Socket(serverIpAddress, serverPort)) {
            BufferedReader bufferedReader = buildInputStream(socket);
            outputStream = buildOutputStream(socket);
            logger.log("[Клиент запущен]: SERVER_IP_ADDRESS=" + serverIpAddress + " SERVER_PORT=" + serverPort);
            final MessageReceiver messageReceiver = new MessageReceiver(bufferedReader);
            messageReceiver.start();
            logger.log("[messageReceiver запущен]: ");
            String userMessage = readUserMessage(scanner);
            logger.log(
                    "[Отправлено на сервер]: "
                            + sentMessageToServer(userMessage, outputStream)
            );

            while (!isExit(userMessage)) {
                userMessage = readUserMessage(scanner);
                logger.log(
                        "[Получено с сервера]: "
                                + userMessage);
                sentMessageToServer(userMessage, outputStream);
            }
            scanner.close();
            logger.log("[сканер закрыт]");

        } catch (IOException exception) {
            logger.log("[Подключение к серверу отклонено]" + exception);
            exception.printStackTrace();
        }
    }


    public static boolean isExit(String userMessage) {
        return "exit".equalsIgnoreCase(userMessage);
    }

    public static BufferedReader buildInputStream(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
    }

    public static PrintWriter buildOutputStream(Socket socket) throws IOException {
        return new PrintWriter(
                socket.getOutputStream(), true);
    }

    public static String sentMessageToServer(String userMessage, PrintWriter outputStream) {

        outputStream.println(userMessage);
        return userMessage;
    }

    public static String readUserMessage(Scanner scanner) {
        return scanner.nextLine();
    }

}
