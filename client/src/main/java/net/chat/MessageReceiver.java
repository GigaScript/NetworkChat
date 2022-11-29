package net.chat;


import java.io.BufferedReader;
import java.io.IOException;


public class MessageReceiver extends Thread {
    private static BufferedReader bufferedReader;
    private static final ClientSetting CLIENT_SETTING = ClientSetting.getInstance();
    private static Logger logger = null;

    public MessageReceiver(BufferedReader bufferedReader) {
        MessageReceiver.bufferedReader = bufferedReader;
        logger = ConsoleClient.getLogger();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println(receiveMessageFromServer());
            } catch (IOException e) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    logger.log("Сервер прервал соединение c Клиентом");
                    throw new RuntimeException("Сервер прервал соединение");
                }
            }
        }
    }

    public static String receiveMessageFromServer() throws IOException {
        return logger.logWhitReturn(
                "Получено с сервера",
                bufferedReader.readLine()
        );
    }

}
