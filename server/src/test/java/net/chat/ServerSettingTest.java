package net.chat;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class ServerSettingTest {
    String propertyFilePath = "./setting.properties";
    ServerSetting serverSetting = ServerSetting.getInstance(propertyFilePath);
    @Test
    void getInstance() {
        assertNotNull(ServerSetting.getInstance(propertyFilePath));
    }

    @Test
    void serverIpAddress() throws UnknownHostException {
        InetAddress serverIpAddressExpected = InetAddress.getByName("localhost");
        InetAddress serverIpAddressActual = serverSetting.serverIpAddress();
        assertEquals(serverIpAddressActual, serverIpAddressExpected);
        serverIpAddressExpected = InetAddress.getByName("127.1.1.1");
        assertNotEquals(serverIpAddressActual,serverIpAddressExpected);
    }

    @Test
    void getServerPort() {
        int serverPortExpected = 9999;
        int serverPortActual = serverSetting.getServerPort();
        assertNotEquals(serverPortExpected,serverPortActual);
        serverPortExpected = 2020;
        assertEquals(serverPortExpected,serverPortActual);
    }

    @Test
    void getLogPath() {
        String logPathExpected = "./server/src/main/java/net/chat/resources/log.txt";
        String logPathActual = serverSetting.getLogPath();
        assertEquals(logPathExpected,logPathActual);
        logPathExpected = "./servers/src/main/java/net/chat/resources/log.txt";
        assertNotEquals(logPathExpected,logPathActual);
    }
}