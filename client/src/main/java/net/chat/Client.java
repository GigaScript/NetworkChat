package net.chat;

public class Client {
    final static String propertyFilePath = "./client/src/main/java/net/chat/resources/setting.properties";
    final static ClientSetting setting = ClientSetting.getInstance(propertyFilePath);
    final static Logger logPath = new Logger(setting.getLogPath());
    public static void main(String[] args) {
        ConsoleClient consoleClient = new ConsoleClient(setting.serverIpAddress(),setting.getServerPort(), logPath);
        consoleClient.startClient();
    }
}
