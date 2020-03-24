package hyperdefined.anarchystats;

import org.bukkit.plugin.java.JavaPlugin;

public class AnarchyStats extends JavaPlugin {

    private static AnarchyStats anarchyStats;

    public long worldSize;

    public static AnarchyStats getInstance() {
        return anarchyStats;
    }

    @Override
    public void onEnable() {
        anarchyStats = this;
        worldSize = WorldSize.getWorldSize(WorldSize.WORLD, WorldSize.WORLD_NETHER, WorldSize.WORLD_THE_END);

        System.out.println("[AnarchyStats] Plugin created by hyperdefined.");
        this.getCommand("info").setExecutor(new CommandStats());
    }

    @Override
    public void onDisable() {

    }
}