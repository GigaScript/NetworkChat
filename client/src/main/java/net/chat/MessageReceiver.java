package net.chat;


import java.io.BufferedReader;
import java.io.IOException;


public class MessageReceiver extends Thread {
    private final BufferedReader bufferedReader;
    private static final Logger logger = Logger.getInstance();

    public MessageReceiver(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String messageFromServer = receiveMessageFromServer(bufferedReader);
                logger.log("[Получено с сервера]: " + messageFromServer);
                System.out.println(messageFromServer);
            } catch (IOException e) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    logger.log("[Сервер прервал соединение c Клиентом]");
                    throw new RuntimeException("[Сервер прервал соединение]");
                }
            }
        }
    }

    public static String receiveMessageFromServer(BufferedReader bufferedReader) throws IOException {
        return bufferedReader.readLine();
    }

}
