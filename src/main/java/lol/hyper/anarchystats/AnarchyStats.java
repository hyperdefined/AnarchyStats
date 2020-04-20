package lol.hyper.anarchystats;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class AnarchyStats extends JavaPlugin {

    private static AnarchyStats anarchyStats;

    public String worldSize;

    public static AnarchyStats getInstance() {
        return anarchyStats;
    }

    @Override
    public void onEnable() {
        anarchyStats = this;
        Logger logger = this.getLogger();

        new Updater(this, 66089).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("There is not a new update available.");
            } else {
                logger.info("There is a new update available!");
            }
        });
        getWorldSize();

        this.getCommand("info").setExecutor(new CommandStats());
    }

    @Override
    public void onDisable() {

    }

    public void getWorldSize() {
        worldSize = WorldSize.humanReadableByteCount(WorldSize.getWorldSize(WorldSize.WORLD, WorldSize.WORLD_NETHER, WorldSize.WORLD_THE_END), false);
    }
}