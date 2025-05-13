package me.josielcm.jcm;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;
import me.josielcm.jcm.logger.Log;
import me.josielcm.jcm.logger.Log.LogLevel;
import me.josielcm.jcm.regions.selector.SelectorManager;
import me.josielcm.jcm.utils.Key;

/**
 * Clase principal de la API que sigue el patrón Singleton.
 * Proporciona acceso centralizado a las funcionalidades de la API.
 * 
 * @author JosielCM
 */
public class JCoreAPI {

    @Getter
    private static final JCoreAPI instance = new JCoreAPI();

    @Getter
    private static JavaPlugin plugin;
    
    @Getter
    private static SelectorManager selectorManager;
    
    @Getter
    @Setter
    private static boolean enabled = false;

    @Getter
    @Setter
    private static boolean debug = false;

    private JCoreAPI() {}

    /**
     * Inicializa de la API
     * @param pluginInstance El plugin que está inicializando la API
     */
    public static void init(JavaPlugin pluginInstance) {
        
        if (isEnabled()) {
            Log.log(LogLevel.WARNING, "JCoreAPI is already initialized and cannot be initialized again.");
            Log.onInit(true);
            return;
        }

        if (pluginInstance == null) {
            Log.log(LogLevel.ERROR, "JCoreAPI cannot be initialized with a null plugin instance.");
            Log.onInit(true);
            return;
        }

        plugin = pluginInstance;
        
        Key.instanceKeys(plugin);
        selectorManager = new SelectorManager();

        setEnabled(true);
        Log.onInit(!enabled);
    }

    public static void shutdown() {
        setEnabled(false);
        Log.onShutdown();
    }
}