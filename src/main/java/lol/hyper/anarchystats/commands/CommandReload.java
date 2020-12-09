package lol.hyper.anarchystats.commands;

import lol.hyper.anarchystats.AnarchyStats;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

public class CommandReload implements TabExecutor {

    private final AnarchyStats anarchyStats;

    public CommandReload (AnarchyStats anarchyStats) {
        this.anarchyStats = anarchyStats;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.GREEN + "AnarchyStats version " + anarchyStats.getDescription().getVersion() + ". Created by hyperdefined.");
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("anarchystats.reload")) {
                    anarchyStats.loadConfig();
                    sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.singletonList("reload");
    }
}
