package lol.hyper.anarchystats;

import lol.hyper.anarchystats.commands.CommandInfo;
import lol.hyper.anarchystats.commands.CommandReload;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Logger;

public final class AnarchyStats extends JavaPlugin {

    public static String worldSize;
    public final File configFile = new File(this.getDataFolder(), "config.yml");
    public FileConfiguration config;
    public final Logger logger = this.getLogger();
    public final ArrayList<Path> worldPaths = new ArrayList<>();

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
        Command commandTest = Bukkit.getPluginCommand(config.getString("info-command-override"));
        if (commandTest != null) {
            logger.warning("We detected that /" + config.getString("info-command-override") + " is already taken by another plugin. There might be some issues with this. To change this, edit the info-command-override setting in the config.");
        }

        this.getCommand(config.getString("info-command-override")).setExecutor(commandInfo);
        this.getCommand("anarchystats").setExecutor(commandReload);
        Bukkit.getScheduler().runTaskAsynchronously(this, this::updateWorldSize);

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

    public void updateWorldSize() {
        worldSize = WorldSize.readableFileSize(WorldSize.getWorldSize(worldPaths));
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        if (worldPaths.size() > 0) {
            worldPaths.clear();
        }
        for (String x : config.getStringList("worlds-to-use")) {
            Path currentPath = Paths.get(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + x);
            if (!currentPath.toFile().exists()) {
                logger.warning("World folder \"" + x + "\" does not exist! Excluding from size calculation.");
            } else {
                worldPaths.add(currentPath);
            }
        }
    }
}