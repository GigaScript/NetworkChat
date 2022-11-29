package net.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class ConsoleClient {
    private static final ClientSetting CLIENT_SETTING = ClientSetting.getInstance();
    private static final InetAddress SERVER_IP_ADDRESS = CLIENT_SETTING.serverIpAddress();
    private static final int SERVER_PORT = CLIENT_SETTING.getServerPort();
    private static final Logger LOGGER = new Logger(CLIENT_SETTING.getLogPath());
    private static final Scanner scanner = new Scanner(System.in);
    private static PrintWriter printWriter;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP_ADDRESS, SERVER_PORT)) {
            BufferedReader bufferedReader = createBufferedReader(socket);
            printWriter = createPrintWriter(socket);
            LOGGER.log("Клиент запущен: SERVER_IP_ADDRESS=" + SERVER_IP_ADDRESS + " SERVER_PORT=" + SERVER_PORT);
            final MessageReceiver messageReceiver = new MessageReceiver(bufferedReader);
            messageReceiver.start();
            LOGGER.log("messageReceiver запущен");
            String userMessage = readUserMessage(scanner);
            sentMessageToServer(userMessage);
            while (!isExit(userMessage)) {
                userMessage = readUserMessage(scanner);
                sentMessageToServer(userMessage);
            }
            closeScanner(scanner);

        } catch (IOException exception) {
            LOGGER.log("Подключение к серверу отклонено" + exception);
            exception.printStackTrace();
        }
    }


    public static boolean isExit(String userMessage) {
        return "exit".equalsIgnoreCase(userMessage);
    }

    public static BufferedReader createBufferedReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
    }

    public static PrintWriter createPrintWriter(Socket socket) throws IOException {
        return new PrintWriter(
                socket.getOutputStream(), true);
    }

    public static void sentMessageToServer(String userMessage) {
        LOGGER.log("Отправлено на сервер: " + userMessage);
        printWriter.println(userMessage);
    }

    public static String readUserMessage(Scanner scanner) {
        return LOGGER.logWhitReturn(
                "Пользователь отправил сообщение: "
                , scanner.nextLine()
        );
    }

    public static void closeScanner(Scanner scanner) {
        LOGGER.log("Работа клиента остановлена");
        scanner.close();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
