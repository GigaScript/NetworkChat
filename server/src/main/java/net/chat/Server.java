package net.chat;

import java.io.IOException;

public class Server {
    final static String propertyFilePath = "./server/src/main/java/net/chat/resources/setting.properties";
    final static ServerSetting setting = ServerSetting.getInstance(propertyFilePath);
    final static Logger logger = new Logger(setting.getLogPath());

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer(setting.serverIpAddress(),setting.getServerPort(), logger);
        chatServer.startServer();
    }
}
