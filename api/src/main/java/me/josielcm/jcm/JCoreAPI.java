package me.josielcm.jcm;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import me.josielcm.jcm.logger.Log;
import me.josielcm.jcm.logger.Log.LogLevel;
import me.josielcm.jcm.regions.selector.SelectorManager;
import me.josielcm.jcm.utils.Key;

public class JCoreAPI {

    @Getter
    private static JCoreAPI instance;

    @Getter
    private static SelectorManager selectorManager;
    
    @Getter
    @Setter
    private static boolean enabled = false;

    @Getter
    @Setter
    private static boolean debug = false;

    @Getter
    @Setter
    private JavaPlugin plugin;

    public static void init(JavaPlugin plugin) {
        if (instance == null) {
            instance = new JCoreAPI();
        }

        if (isEnabled()) {
            Log.log(LogLevel.WARNING, "JCoreAPI is already initialized cannot be initialized again.");
            return;
        }

        if (plugin == null) {
            Log.log(LogLevel.ERROR, "Error loading JCoreAPI: plugin cannot be null");
            return;
        }

        Key.instanceKeys(plugin);
        selectorManager = new SelectorManager();

        setEnabled(true);
        Log.onInit(!isEnabled());
    }

    public static void shutdown() {
        if (instance == null) {
            return;
        }

        instance = null;
        setEnabled(false);
        Log.onShutdown();
    }

}
