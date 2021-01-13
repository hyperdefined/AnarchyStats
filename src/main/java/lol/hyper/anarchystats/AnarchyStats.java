package lol.hyper.anarchystats;

import lol.hyper.anarchystats.commands.CommandInfo;
import lol.hyper.anarchystats.commands.CommandReload;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public final class AnarchyStats extends JavaPlugin {

    public static String worldSize;
    public final File configFile = new File(this.getDataFolder(), "config.yml");
    public FileConfiguration config;
    public final Logger logger = this.getLogger();

    public CommandInfo commandInfo;
    public CommandReload commandReload;
    public MessageParser messageParser;

    @Override
    public void onEnable() {
        messageParser = new MessageParser(this);
        commandInfo = new CommandInfo(this, messageParser);
        commandReload = new CommandReload(this);
        if (!configFile.exists()) {
            this.saveResource("config.yml", true);
            logger.info("Copying default config!");
        }
        loadConfig();
        this.getCommand("info").setExecutor(commandInfo);
        this.getCommand("anarchystats").setExecutor(commandReload);
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> this.updateWorldSize(WorldSize.world, WorldSize.nether, WorldSize.end));

        new Updater(this, 66089).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info("You are running the latest version.");
            } else {
                logger.info("There is a new version available! Please download at https://www.spigotmc.org/resources/anarchystats.66089/");
            }
        });
        Metrics metrics = new Metrics(this, 6877);
    }

    @Override
    public void onDisable() {
    }

    public void updateWorldSize(Path world, Path nether, Path end) {
        worldSize = WorldSize.readableFileSize(WorldSize.getWorldSize(world) + WorldSize.getWorldSize(nether) + WorldSize.getWorldSize(end));
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        WorldSize.world = Paths.get(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + config.getString("world-files.overworld"));
        WorldSize.nether = Paths.get(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + config.getString("world-files.nether"));
        WorldSize.end = Paths.get(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + config.getString("world-files.end"));
    }
}
