package main.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
    private static final Logger LOGGER = Logger.getLogger(LogManager.class.getName());
    private static final String LOG_FILE = "logs/application.log";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            // Créer le dossier logs s'il n'existe pas
            Path logPath = Paths.get("logs");
            if (!Files.exists(logPath)) {
                Files.createDirectories(logPath);
            }

            // Configurer le logger
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation des logs : " + e.getMessage());
        }
    }

    public static void logInfo(String message) {
        String logMessage = formatLogMessage("INFO", message);
        LOGGER.info(logMessage);
        writeToFile(logMessage);
    }

    public static void logError(String message, Throwable error) {
        String logMessage = formatLogMessage("ERROR", message + " - " + error.getMessage());
        LOGGER.severe(logMessage);
        writeToFile(logMessage);
    }

    public static void logOperation(String operation, String details) {
        String logMessage = formatLogMessage("OPERATION", operation + " - " + details);
        LOGGER.info(logMessage);
        writeToFile(logMessage);
    }

    private static String formatLogMessage(String level, String message) {
        return String.format("[%s] %s - %s",
            LocalDateTime.now().format(DATE_FORMAT),
            level,
            message
        );
    }

    private static synchronized void writeToFile(String message) {
        try {
            Files.write(
                Paths.get(LOG_FILE),
                (message + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Erreur d'écriture dans le fichier de log : " + e.getMessage());
        }
    }
}