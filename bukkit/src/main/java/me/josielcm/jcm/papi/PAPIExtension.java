package me.josielcm.jcm.papi;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.josielcm.jcm.logger.Log;

public class PAPIExtension extends PlaceholderExpansion {

    private final String identifier;
    private final String author;
    private final String version;

    @Getter
    private Map<String, String> staticPlaceholders;
    
    private Map<Pattern, BiFunction<OfflinePlayer, Matcher, String>> patternHandlers;

    public PAPIExtension(String identifier, String author, String version) {
        this.identifier = identifier;
        this.author = author;
        this.version = version;
        staticPlaceholders = new HashMap<>();
        patternHandlers = new HashMap<>();
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    @Override
    public @NotNull String getAuthor() {
        return author;
    }

    @Override
    public @NotNull String getVersion() {
        return version;
    }

    @Override
    public boolean persist() {
        return true;
    }
    
    /**
     * Añade un placeholder estático simple
     * @param placeholder El nombre del placeholder (sin el prefijo %)
     * @param value El valor que se mostrará
     */
    public void addPlaceholder(String placeholder, String value) {
        staticPlaceholders.put(placeholder, value);
    }
    
    /**
     * Registra un manejador de placeholder basado en un patrón
     * @param pattern Patrón regex para coincidir con el placeholder
     * @param handler Función que recibe el jugador y el matcher para extraer grupos
     * @author JosielCM
     * 
     * <h3>Ejemplos comunes de patrones regex:</h3>
     * <ul>
     *   <li><code>(.+)</code>: Captura cualquier texto (uno o más caracteres)
     *       <br>Ejemplo: <code>"player_(.+)"</code> captura "Steve" en "player_Steve"</li>
     *   <li><code>(\\d+)</code>: Captura uno o más dígitos numéricos
     *       <br>Ejemplo: <code>"top_(\\d+)"</code> captura "5" en "top_5"</li>
     *   <li><code>([^_]+)</code>: Captura texto hasta encontrar un guion bajo
     *       <br>Ejemplo: <code>"stats_([^_]+)_(.+)"</code> captura "pvp" y "kills" en "stats_pvp_kills"</li>
     *   <li><code>([A-Za-z]+)</code>: Captura solo letras (A-Z, a-z)
     *       <br>Ejemplo: <code>"rank_([A-Za-z]+)"</code> captura "admin" en "rank_admin"</li>
     *   <li><code>([\\w-\\.]+)</code>: Captura letras, números, guiones y puntos
     *       <br>Ejemplo: <code>"item_([\\w-\\.]+)"</code> captura "diamond.sword-epic" en "item_diamond.sword-epic"</li>
     * </ul>
     * 
     * <h3>Ejemplo de uso:</h3>
     * <pre>
     * // Registrar el patrón para placeholders tipo "playtime_nombredelJugador"
     * registerPatternHandler("playtime_(.+)", (player, matcher) -> {
     *     String playerName = matcher.group(1); // Esto captura el nombre del jugador
     *     
     *     // Aquí buscas el tiempo de juego para ese jugador
     *     // Por ejemplo:
     *     return getPlayerPlaytime(playerName);
     * });
     * 
     * // Patrón para top_categoría_posición
     * registerPatternHandler("top_([^_]+)_(\\d+)", (player, matcher) -> {
     *     String category = matcher.group(1); // Captura la categoría (ej: "kills")
     *     int position = Integer.parseInt(matcher.group(2)); // Captura la posición (ej: "1")
     *     return getTopPlayerInCategory(category, position);
     * });
     * 
     * // Patrón que acepta caracteres especiales como guiones, puntos, etc.
     * registerPatternHandler("item_([\\w\\-\\.]+)", (player, matcher) -> {
     *     String itemId = matcher.group(1); // Captura identificadores como "diamond.sword-epic"
     *     return getItemInfo(itemId);
     * });
     * </pre>
     */
    public void registerPatternHandler(String pattern, BiFunction<OfflinePlayer, Matcher, String> handler) {
        if (pattern == null || handler == null) {
            throw new IllegalArgumentException("Pattern and handler cannot be null");
        }
        patternHandlers.put(Pattern.compile(pattern), handler);
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player == null) {
            return "";
        }

        if (staticPlaceholders.containsKey(params)) {
            return staticPlaceholders.get(params);
        }
        
        for (Map.Entry<Pattern, BiFunction<OfflinePlayer, Matcher, String>> entry : patternHandlers.entrySet()) {
            Matcher matcher = entry.getKey().matcher(params);
            if (matcher.matches()) {
                try {
                    String result = entry.getValue().apply(player, matcher);
                    if (result != null) {
                        return result;
                    }
                } catch (Exception e) {
                    Log.logError("Error processing placeholder: " + params, e);
                }
            }
        }
        
        // Placeholder de test por defecto
        if (params.equals("test")) {
            return getIdentifier() + " by " + getAuthor() + " v" + getVersion();
        }

        return null;
    }
    
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
}