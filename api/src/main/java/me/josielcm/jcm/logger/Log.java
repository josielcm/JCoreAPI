package me.josielcm.jcm.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.josielcm.jcm.JCoreAPI;
import me.josielcm.jcm.formats.Color;

public class Log {

    public enum LogLevel {
        INFO("<gold>INFO: </gold>", "<gold>"),
        WARNING("<yellow>WARNING: </yellow>", "<yellow>"),
        ERROR("<red>ERROR: </red>", "<red>"),
        DEBUG("<aqua>DEBUG: </aqua>", "<aqua>"),
        FATAL("<dark_red>FATAL: </dark_red>", "<dark_red>"),
        SUCCESS("<green>SUCCESS: </green>", "<green>"),;

        @Getter
        private String prefix;

        @Getter
        private String color;

        LogLevel(String prefix, String color) {
            this.prefix = prefix;
            this.color = color;
        }
    }

    public static void onInit(boolean errorLoading) {
        if (errorLoading) {
            logHeader(LogLevel.ERROR, "JCoreAPI - Error");
            log(LogLevel.ERROR, "JCoreAPI failed to load.");
            logFooter(LogLevel.ERROR);
        } else {
            logHeader(LogLevel.INFO, "JCoreAPI - API");
            log(LogLevel.INFO, "API initialized successfully.");
            logFooter(LogLevel.INFO);
        }
    }

    public static void onShutdown() {
        logHeader(LogLevel.INFO, "JCoreAPI - API");
        log(LogLevel.INFO, "API shutdown successfully.");
        logFooter(LogLevel.INFO);
    }
    
    public static void log(LogLevel level, String message)  {
        JCoreAPI.getInstance().getPlugin().getServer().getConsoleSender().sendMessage(Color.parse(level.getPrefix() + message));
    }

    public static void log(String message) {
        JCoreAPI.getInstance().getPlugin().getServer().getConsoleSender().sendMessage(Color.parse(LogLevel.INFO.getPrefix() + message));
    }

    public static void logError(String message, Exception e) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formater);

        logHeader(LogLevel.ERROR, "JCoreAPI - Error");
        log(LogLevel.ERROR, "Timestamp: " + timestamp);
        log(LogLevel.ERROR, "Message: " + message);

        if (e != null) {
            log(LogLevel.ERROR, "Exception: " + e.getClass().getName());
            log(LogLevel.ERROR, "Cause: " + e.getCause());
            log(LogLevel.ERROR, "Message: " + e.getMessage());
            log(LogLevel.ERROR, "Stacktrace: ");
            for (StackTraceElement element : e.getStackTrace()) {
                log(LogLevel.ERROR, "  at " + element.toString());
            }
        }

        Bukkit.getConsoleSender().sendMessage(Color.parse("<grey>Check the console for more details."));
    }

    private static void logHeader(LogLevel level, String title) {
        String seperator = "-".repeat(50);

        Bukkit.getConsoleSender().sendMessage(Color.parse(level.getColor() + seperator));
        Bukkit.getConsoleSender().sendMessage(Color.parse(level.getColor() + " " + title));
        Bukkit.getConsoleSender().sendMessage(Color.parse(level.getColor() + seperator));
    }

    private static void logFooter(LogLevel level) {
        String seperator = "-".repeat(50);
        Bukkit.getConsoleSender().sendMessage(Color.parse(level.getColor() + seperator));
        Bukkit.getConsoleSender().sendMessage(Color.parse(""));
    }

}
