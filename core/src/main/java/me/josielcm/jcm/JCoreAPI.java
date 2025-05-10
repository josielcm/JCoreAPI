package me.josielcm.jcm;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;

public class JCoreAPI {

    @Getter
    private static JCoreAPI instance;
    
    @Getter
    @Setter
    private boolean enabled = false;


    public static void initialize(Logger logger) {
        if (instance == null) {
            instance = new JCoreAPI();
        }

        if (instance.isEnabled()) {
            logger.log(Level.WARNING, "JCoreAPI is already initialized.");
            return;
        }

        instance.setEnabled(true);
        logger.log(Level.INFO, "JCoreAPI initialized successfully.");
    }

    public static void shutdown(Logger logger) {
        if (instance == null) {
            logger.log(Level.WARNING, "JCoreAPI is not initialized.");
            return;
        }

        instance.setEnabled(false);
        logger.log(Level.INFO, "JCoreAPI shut down successfully.");
    }

}
