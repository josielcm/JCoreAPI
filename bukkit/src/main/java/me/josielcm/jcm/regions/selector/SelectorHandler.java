package me.josielcm.jcm.regions.selector;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import me.josielcm.jcm.JCoreAPI;
import me.josielcm.jcm.formats.Color;
import me.josielcm.jcm.logger.Log;
import me.josielcm.jcm.logger.Log.LogLevel;
import me.josielcm.jcm.regions.Cuboid;

public class SelectorHandler {

    public static void handleLeftClick(Player player, PlayerInteractEvent event) {
        SelectorManager selectorManager = JCoreAPI.getSelectorManager();
        SelectorContainer selectorContainer = selectorManager.getSelectorContainers().get(player.getUniqueId());

        if (selectorContainer == null) {
            Log.log(LogLevel.WARNING, "SelectorContainer not found for player: " + player.getName());
            return;
        }

        selectorContainer.setTempPos1(event.getClickedBlock().getLocation());
        player.sendMessage(
                Color.parse("<gold>Selector position 1 setted to x: " + event.getClickedBlock().getX() + " y: "
                        + event.getClickedBlock().getY() + " z: " + event.getClickedBlock().getZ()));
    }

    public static void handleRightClick(Player player, PlayerInteractEvent event) {
        SelectorManager selectorManager = JCoreAPI.getSelectorManager();
        SelectorContainer selectorContainer = selectorManager.getSelectorContainers().get(player.getUniqueId());

        if (selectorContainer == null) {
            Log.log(LogLevel.WARNING, "SelectorContainer not found for player: " + player.getName());
            return;
        }

        selectorContainer.setTempPos2(event.getClickedBlock().getLocation());
        player.sendMessage(
                Color.parse("<gold>Selector position 2 setted to x: " + event.getClickedBlock().getX() + " y: "
                        + event.getClickedBlock().getY() + " z: " + event.getClickedBlock().getZ()));
    }

    public static void handleShiftLeftClick(Player player, PlayerInteractEvent event) {
        SelectorManager selectorManager = JCoreAPI.getSelectorManager();
        SelectorContainer selectorContainer = selectorManager.getSelectorContainers().get(player.getUniqueId());

        if (selectorContainer == null) {
            Log.log(LogLevel.WARNING, "SelectorContainer not found for player: " + player.getName());
            return;
        }

        if (selectorContainer.getTempPos1() == null || selectorContainer.getTempPos2() == null) {
            player.sendMessage(Color.parse("<red>First set the positions using left and right click!"));
            return;
        }

        selectorContainer.setTempPos1(event.getClickedBlock().getLocation());
        selectorContainer.setCuboid(new Cuboid(selectorContainer.getTempPos1(), selectorContainer.getTempPos2()));
        player.sendMessage(Color.parse("<gold>Selector cuboid created"));
    }

    public static void handleShiftRightClick(Player player, PlayerInteractEvent event) {
        SelectorManager selectorManager = JCoreAPI.getSelectorManager();
        SelectorContainer selectorContainer = selectorManager.getSelectorContainers().get(player.getUniqueId());

        if (selectorContainer == null) {
            Log.log(LogLevel.WARNING, "SelectorContainer not found for player: " + player.getName());
            return;
        }

        if (selectorContainer.getTempPos1() == null || selectorContainer.getTempPos2() == null) {
            player.sendMessage(Color.parse("<red>First set the positions using left and right click!"));
            return;
        }

        selectorContainer.setTempPos2(event.getClickedBlock().getLocation());
        selectorContainer.setCuboid(new Cuboid(selectorContainer.getTempPos1(), selectorContainer.getTempPos2()));
        player.sendMessage(Color.parse("<gold>Selector cuboid created"));
    }

}
