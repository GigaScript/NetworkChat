package net.chat.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

public class ServerSetting {
    private final File settingFile = new File("./src/main/java/net/chat/resources/setting.properties");
    private final Properties properties = new Properties();
    private InetAddress serverIp;
    private int serverPort;
    private String logPath;

    public ServerSetting() throws IOException {
        loadSettingFromFile();
    }

    private void loadSettingFromFile() throws IOException {
        properties.load(new FileReader(settingFile));
        serverIp = InetAddress.getByName(
                properties.getProperty("ipAddress", "localhost")
        );
        serverPort = Integer.parseInt(properties.getProperty("port", "2022"));
        logPath = properties.getProperty("logPath", "./src/main/java/net/chat/resources/log.txt");
    }

    public InetAddress getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getLogPath() {
        return logPath;
    }
}
