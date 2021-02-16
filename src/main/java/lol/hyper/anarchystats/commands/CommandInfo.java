package lol.hyper.anarchystats.commands;

import lol.hyper.anarchystats.AbstractCommand;
import lol.hyper.anarchystats.AnarchyStats;
import lol.hyper.anarchystats.MessageParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandInfo extends AbstractCommand {

    private final AnarchyStats anarchyStats;

    public CommandInfo(String command, AnarchyStats anarchyStats) {
        super(command);
        this.anarchyStats = anarchyStats;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (anarchyStats.config.getBoolean("use-permission-node")) {
            if (!sender.hasPermission(anarchyStats.config.getString("permission-node"))) {
                sender.sendMessage(ChatColor.RED + "You don't have permission for this command.");
                return true;
            }
        }
        Bukkit.getScheduler().runTaskAsynchronously(anarchyStats, anarchyStats::updateWorldSize);
        for (String x : anarchyStats.messageParser.getCommandMessage()) {
            sender.sendMessage(x);
        }
        return true;
    }
}