package me.josielcm.jcm.formats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Utilidad para el formateo de diferentes tipos de datos
 * 
 * @author JosielCM
 */
public class Format {

    private static final Pattern SPACING_CHARS_REGEX = Pattern.compile("[_ \\-]+");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.##");
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Formatea un tiempo en segundos a un formato legible
     * 
     * @param seconds El tiempo en segundos
     * @return String formateado (ej: "01:02:03:04" para 1 día, 2 horas, 3 minutos, 4 segundos)
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * // Formatear 90061 segundos (1 día, 1 hora, 1 minuto, 1 segundo)
     * String formattedTime = Format.formatTime(90061); 
     * // Resultado: "01:01:01:01"
     * </pre>
     */
    public static String formatTime(int seconds) {
        int days = seconds / 86400;
        int hours = (seconds % 86400) / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        
        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, secs);
    }
    
    /**
     * Formatea un tiempo en milisegundos a un formato legible con unidades
     * 
     * @param milliseconds El tiempo en milisegundos
     * @param shortFormat Si es true, usa formato corto (1d 2h 3m), si es false usa formato largo (1 día 2 horas 3 minutos)
     * @return String formateado con unidades
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * // Formato corto
     * String shortTime = Format.formatTimeWithUnits(90061000, true); 
     * // Resultado: "1d 1h 1m 1s"
     * 
     * // Formato largo
     * String longTime = Format.formatTimeWithUnits(90061000, false);
     * // Resultado: "1 día 1 hora 1 minuto 1 segundo"
     * </pre>
     */
    public static String formatTimeWithUnits(long milliseconds, boolean shortFormat) {
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        milliseconds -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        milliseconds -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        
        StringBuilder sb = new StringBuilder();
        
        if (days > 0) {
            sb.append(days).append(shortFormat ? "d " : (days == 1 ? " día " : " días "));
        }
        if (hours > 0) {
            sb.append(hours).append(shortFormat ? "h " : (hours == 1 ? " hora " : " horas "));
        }
        if (minutes > 0) {
            sb.append(minutes).append(shortFormat ? "m " : (minutes == 1 ? " minuto " : " minutos "));
        }
        if (seconds > 0) {
            sb.append(seconds).append(shortFormat ? "s" : (seconds == 1 ? " segundo" : " segundos"));
        }
        
        return sb.toString().trim();
    }
    
    /**
     * Formatea una duración entre dos fechas
     * 
     * @param start Fecha de inicio
     * @param end Fecha de fin
     * @param shortFormat Si es true, usa formato corto
     * @return String con la duración formateada
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * LocalDateTime start = LocalDateTime.now();
     * LocalDateTime end = start.plusDays(1).plusHours(2).plusMinutes(30);
     * String duration = Format.formatDuration(start, end, true);
     * // Resultado: "1d 2h 30m"
     * </pre>
     */
    public static String formatDuration(LocalDateTime start, LocalDateTime end, boolean shortFormat) {
        Duration duration = Duration.between(start, end);
        return formatTimeWithUnits(duration.toMillis(), shortFormat);
    }

    /**
     * Elimina caracteres de espaciado (espacios, guiones bajos, guiones)
     * 
     * @param string El texto a procesar
     * @return El texto sin caracteres de espaciado
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.removeSpacingChars("hello_world this-is a-test");
     * // Resultado: "helloworldthisisatest"
     * </pre>
     */
    public static String removeSpacingChars(String string) {
        if (string == null) return null;
        return SPACING_CHARS_REGEX.matcher(string).replaceAll("");
    }
    
    /**
     * Formatea un número con separadores de miles y decimales
     * 
     * @param number El número a formatear
     * @return El número formateado
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.formatNumber(1234567.89);
     * // Resultado: "1,234,567.89"
     * </pre>
     */
    public static String formatNumber(double number) {
        return DECIMAL_FORMAT.format(number);
    }
    
    /**
     * Formatea un número con abreviatura K, M, B, T para miles, millones, etc.
     * 
     * @param number El número a formatear
     * @return El número formateado con abreviaturas
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.formatNumberCompact(1234567);
     * // Resultado: "1.23M"
     * </pre>
     */
    public static String formatNumberCompact(double number) {
        if (number < 1000) return String.valueOf((int) number);
        
        int exp = (int) (Math.log(number) / Math.log(1000));
        char suffix = "KMBTQ".charAt(exp - 1);
        return String.format("%.2f%c", number / Math.pow(1000, exp), suffix)
            .replace(".00", "");
    }
    
    /**
     * Formatea una fecha usando el formato predeterminado (dd/MM/yyyy HH:mm:ss)
     * 
     * @param dateTime La fecha a formatear
     * @return La fecha formateada
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.formatDateTime(LocalDateTime.now());
     * // Resultado: "11/05/2025 15:30:45"
     * </pre>
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_DATE_FORMATTER);
    }
    
    /**
     * Formatea una fecha usando un patrón personalizado
     * 
     * @param dateTime La fecha a formatear
     * @param pattern El patrón a usar (ej: "yyyy-MM-dd")
     * @return La fecha formateada
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.formatDateTime(LocalDateTime.now(), "yyyy-MM-dd");
     * // Resultado: "2025-05-11"
     * </pre>
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    
    /**
     * Convierte un UUID a su formato canónico (con guiones)
     * 
     * @param uuid El UUID a formatear
     * @return El UUID formateado
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * UUID uuid = UUID.randomUUID();
     * String result = Format.formatUUID(uuid);
     * // Resultado: "f81d4fae-7dec-11d0-a765-00a0c91e6bf6"
     * </pre>
     */
    public static String formatUUID(UUID uuid) {
        return uuid.toString();
    }
    
    /**
     * Formatea un uuid sin guiones
     * 
     * @param uuid El UUID a formatear
     * @return El UUID sin guiones
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * UUID uuid = UUID.randomUUID();
     * String result = Format.formatUUIDWithoutDashes(uuid);
     * // Resultado: "f81d4fae7dec11d0a76500a0c91e6bf6"
     * </pre>
     */
    public static String formatUUIDWithoutDashes(UUID uuid) {
        return uuid.toString().replace("-", "");
    }
    
    /**
     * Capitaliza la primera letra de cada palabra en un texto
     * 
     * @param text El texto a capitalizar
     * @return El texto con la primera letra de cada palabra en mayúscula
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.capitalize("hello world");
     * // Resultado: "Hello World"
     * </pre>
     */
    public static String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        
        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        
        return result.toString();
    }
    
    /**
     * Limita la longitud de un texto y añade puntos suspensivos si es necesario
     * 
     * @param text El texto a limitar
     * @param maxLength La longitud máxima
     * @return El texto limitado con puntos suspensivos si se excede la longitud
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.truncate("Este es un texto muy largo", 10);
     * // Resultado: "Este es..."
     * </pre>
     */
    public static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        
        return text.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Formatea bytes a una representación legible (KB, MB, GB, etc.)
     * 
     * @param bytes El número de bytes
     * @return Una representación legible
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.formatFileSize(1536000);
     * // Resultado: "1.5 MB"
     * </pre>
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "B";
        return String.format("%.1f %s", bytes / Math.pow(1024, exp), pre);
    }
    
    /**
     * Formatea un porcentaje con control de decimales
     * 
     * @param value El valor (0-1)
     * @param decimals El número de decimales
     * @return El porcentaje formateado
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.formatPercent(0.75642, 1);
     * // Resultado: "75.6%"
     * </pre>
     */
    public static String formatPercent(double value, int decimals) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(decimals);
        return nf.format(value);
    }
    
    /**
     * Formatea un texto para usarlo de manera segura en URLs (slug)
     * 
     * @param text El texto a formatear
     * @return El texto formateado para URL
     * 
     * @author JosielCM
     * 
     * Ejemplo de uso:
     * <pre>
     * String result = Format.formatToSlug("Este es un Título con Eñes y Espacios");
     * // Resultado: "este-es-un-titulo-con-enes-y-espacios"
     * </pre>
     */
    public static String formatToSlug(String text) {
        String normalized = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
            .replaceAll("[^\\p{ASCII}]", "")
            .toLowerCase(Locale.ENGLISH);
        
        return normalized
            .replaceAll("[^a-zA-Z0-9\\s]", "")
            .trim()
            .replaceAll("\\s+", "-");
    }
}