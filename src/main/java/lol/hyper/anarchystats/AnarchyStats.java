/*
 * This file is part of AnarchyStats.
 *
 * AnarchyStats is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AnarchyStats is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AnarchyStats.  If not, see <https://www.gnu.org/licenses/>.
 */

package lol.hyper.anarchystats;

import lol.hyper.anarchystats.commands.CommandInfo;
import lol.hyper.anarchystats.commands.CommandReload;
import lol.hyper.anarchystats.tools.AbstractCommand;
import lol.hyper.anarchystats.tools.MessageParser;
import lol.hyper.anarchystats.tools.WorldSize;
import lol.hyper.hyperlib.HyperLib;
import lol.hyper.hyperlib.bstats.HyperStats;
import lol.hyper.hyperlib.releases.HyperUpdater;
import lol.hyper.hyperlib.utils.TextUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class AnarchyStats extends JavaPlugin {

    public String worldSize;
    public final File configFile = new File(this.getDataFolder(), "config.yml");
    public final ComponentLogger logger = this.getComponentLogger();
    public final List<Path> worldPaths = new ArrayList<>();
    public final int CONFIG_VERSION = 2;
    public FileConfiguration config;
    public CommandReload commandReload;
    public MessageParser messageParser;
    public HyperLib hyperLib;
    public TextUtils textUtils;

    @Override
    public void onEnable() {
        hyperLib = new HyperLib(this);
        hyperLib.setup();

        textUtils = new TextUtils(hyperLib);

        HyperStats stats = new HyperStats(hyperLib, 6877);
        stats.setup();

        messageParser = new MessageParser(this);
        commandReload = new CommandReload(this);
        if (!configFile.exists()) {
            this.saveResource("config.yml", true);
            logger.info("Copying default config!");
        }
        loadConfig();

        AbstractCommand infoCommand = new CommandInfo(config.getString("info-command-override"), this);
        infoCommand.register();

        this.getCommand("anarchystats").setExecutor(commandReload);
        Bukkit.getAsyncScheduler().runNow(this, scheduledTask -> updateWorldSize());

        HyperUpdater updater = new HyperUpdater(hyperLib);
        updater.setGitHub("hyperdefined", "AnarchyStats");
        updater.setModrinth("Z04RmeT7");
        updater.setHangar("AnarchyStats", "paper");
        updater.check();
    }

    public void updateWorldSize() {
        worldSize = WorldSize.readableFileSize(WorldSize.getWorldSize(worldPaths, logger));
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!worldPaths.isEmpty()) {
            worldPaths.clear();
        }
        for (String worldFolder : config.getStringList("worlds-to-use")) {
            Path currentPath = Paths.get(Paths.get(".").toAbsolutePath().normalize() + File.separator + worldFolder);
            if (!currentPath.toFile().exists()) {
                logger.warn("World folder \"{}\" does not exist! Excluding from size calculation.", worldFolder);
            } else {
                logger.info("Adding {}", worldFolder);
                worldPaths.add(currentPath);
            }
        }

        if (config.getInt("config-version") != CONFIG_VERSION) {
            logger.warn("You configuration is out of date! Some features may not work!");
        }
    }
}
