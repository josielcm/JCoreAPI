package me.josielcm.jcm.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.josielcm.jcm.JCoreAPI;
import me.josielcm.jcm.logger.Log;
import me.josielcm.jcm.logger.Log.LogLevel;

public class Key {
    
    @Getter
    public static NamespacedKey selectorKey;

    public static void instanceKeys(JavaPlugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Error loading JCoreAPI: plugin cannot be null");
        }

        selectorKey = new NamespacedKey(plugin, "JCoreAPI_SelectorKey");

        if (JCoreAPI.isDebug()) {
            Log.log(LogLevel.DEBUG, "JCoreAPI namespacedkeys initialized");
        }
    }

}
