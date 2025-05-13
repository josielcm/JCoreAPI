package me.josielcm.jcm.regions.selector;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import me.josielcm.jcm.utils.Key;

public class SelectorEvents implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getItem() == null || !event.getItem().hasItemMeta()
                || event.getItem().getItemMeta().getPersistentDataContainer() == null) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (container.has(Key.selectorKey)) {
                event.setCancelled(true);

                if (player.isSneaking()) {
                    SelectorHandler.handleShiftLeftClick(player, event);
                } else {
                    SelectorHandler.handleLeftClick(player, event);
                }
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (container.has(Key.selectorKey)) {
                event.setCancelled(true);
                
                if (player.isSneaking()) {
                    SelectorHandler.handleShiftRightClick(player, event);
                } else {
                    SelectorHandler.handleRightClick(player, event);
                }
            }

        }
    }

}
