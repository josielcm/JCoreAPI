package me.josielcm.jcm.regions.selector;

import java.util.UUID;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;
import me.josielcm.jcm.regions.Cuboid;

public class SelectorContainer {
 
    @Getter
    @Setter
    private UUID owner;

    @Getter
    @Setter
    private Location tempPos1;

    @Getter
    @Setter
    private Location tempPos2;

    @Getter
    @Setter
    private Cuboid cuboid;

    public SelectorContainer(UUID owner) {
        this.owner = owner;
    }

}
