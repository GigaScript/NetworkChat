package net.chat;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static Logger instance;
    private static String logPath ;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Logger(String logPath) {
        Logger.logPath = logPath;
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger(logPath);
                }
            }
        }
        return instance;
    }

    public boolean log(String message) {
        try (FileWriter fileWriter = new FileWriter(logPath, true)) {
            PrintWriter writer = new PrintWriter(fileWriter);
            writer.write(LocalDateTime.now().format(formatter) + " " + message + "\r\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
