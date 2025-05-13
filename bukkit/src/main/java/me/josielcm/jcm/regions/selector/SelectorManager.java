package me.josielcm.jcm.regions.selector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.josielcm.jcm.JCoreAPI;
import me.josielcm.jcm.formats.Color;
import me.josielcm.jcm.items.ItemBuilder;
import me.josielcm.jcm.utils.Key;

public class SelectorManager {

    @Getter
    private Map<UUID, SelectorContainer> selectorContainers;

    @Getter
    private Map<UUID, ItemStack[]> playerInventories;
    
    public SelectorManager() {
        JCoreAPI.getPlugin().getServer().getPluginManager().registerEvents(new SelectorEvents(), JCoreAPI.getPlugin());
        selectorContainers = new HashMap<>();
        playerInventories = new HashMap<>();
    }

    public SelectorContainer getSelectorContainer(UUID uuid) {
        return selectorContainers.get(uuid);
    }

    public void enable(Player player) {
        if (selectorContainers.containsKey(player.getUniqueId())) {
            player.sendMessage(Color.parse("<red>Selector is already enabled.</red>"));
            return;
        }

        selectorContainers.put(player.getUniqueId(), new SelectorContainer(player.getUniqueId()));
        playerInventories.put(player.getUniqueId(), player.getInventory().getContents());
        giveItems(player);
        player.sendMessage(Color.parse("<green>Selector enabled.</green>"));
        player.sendMessage(Color.parse("<yellow>Left/Right (Shift Click to make cuboid selection) to set positions</yellow>"));
    }

    public void disable(Player player) {
        if (!selectorContainers.containsKey(player.getUniqueId())) {
            player.sendMessage(Color.parse("<red>Selector is not enabled.</red>"));
            return;
        }

        selectorContainers.remove(player.getUniqueId());
        player.getInventory().clear();
        player.getInventory().setContents(playerInventories.get(player.getUniqueId()));
        playerInventories.remove(player.getUniqueId());
        player.sendMessage(Color.parse("<green>Selector disabled.</green>"));
    }

    private void giveItems(Player player) {
        ItemStack selectorItem = ItemBuilder.builder()
                .material(Material.END_ROD)
                .displayName("<gold><b>Selector</b></gold> <grey>|</grey> <yellow>Left/Right (Shift Click) to set positions")
                .pdc(Key.selectorKey, "selector")
                .build();

        player.getInventory().clear();
        player.getInventory().setItem(0, selectorItem);
    }


}
