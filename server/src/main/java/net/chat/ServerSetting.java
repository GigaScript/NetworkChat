package net.chat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

public class ServerSetting {

    private static ServerSetting instance;
    private static final File PROPERTY_FILE_PATH = new File(
            "./server/src/main/java/net/chat/resources/setting.properties");
    private static final Properties PROPERTIES = new Properties();
    private static InetAddress serverIpAddress;
    private static int serverPort;

    private static String logPath;

    private ServerSetting() {
    }

    public static ServerSetting getInstance() {
        if (instance == null) {
            synchronized (ServerSetting.class) {
                if (instance == null) {
                    instance = new ServerSetting();
                    loadSettingFromFile();
                }
            }
        }
        return instance;
    }

    private static void loadSettingFromFile() {
        try (FileReader fileReader = new FileReader(PROPERTY_FILE_PATH)) {
            PROPERTIES.load(fileReader);
            serverIpAddress = InetAddress.getByName(
                    PROPERTIES.getProperty("ipAddress", "localhost"));
            serverPort = Integer.parseInt(PROPERTIES.getProperty("port", "2022"));
            logPath = PROPERTIES.getProperty("logPath", "./server/src/main/java/net/chat/resources/log.txt");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public InetAddress serverIpAddress() {
        return serverIpAddress;
    }

    public int getServerPort() {
        return serverPort;

    }

    public String getLogPath() {
        return logPath;
    }
}
