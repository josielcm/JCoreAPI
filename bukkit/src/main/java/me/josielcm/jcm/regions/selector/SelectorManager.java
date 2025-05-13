package me.josielcm.jcm.regions.selector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.josielcm.jcm.JCoreAPI;
import me.josielcm.jcm.items.ItemBuilder;
import me.josielcm.jcm.utils.Key;

public class SelectorManager {

    @Getter
    private Map<UUID, SelectorContainer> selectorContainers;

    @Getter
    private Map<UUID, ItemStack[]> playerInventories;
    
    public SelectorManager() {
        JCoreAPI.getInstance().getPlugin().getServer().getPluginManager().registerEvents(new SelectorEvents(), JCoreAPI.getInstance().getPlugin());
        selectorContainers = new HashMap<>();
        playerInventories = new HashMap<>();
    }

    public SelectorContainer getSelectorContainer(UUID uuid) {
        return selectorContainers.get(uuid);
    }

    public void enable(Player player) {
        selectorContainers.put(player.getUniqueId(), new SelectorContainer(player.getUniqueId()));
        playerInventories.put(player.getUniqueId(), player.getInventory().getContents());
        giveItems(player);
    }

    public void disable(Player player) {
        selectorContainers.remove(player.getUniqueId());
        player.getInventory().clear();
        player.getInventory().setContents(playerInventories.get(player.getUniqueId()));
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
