package net.chat;

import net.chat.server.Server;
import net.chat.server.ServerSetting;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSetting chatServerSetting = new ServerSetting();
        Server chatServer = new Server(
                chatServerSetting.getServerIp(),
                chatServerSetting.getServerPort()
        );
    }
}