package lol.hyper.anarchystats.commands;

import lol.hyper.anarchystats.AnarchyStats;
import lol.hyper.anarchystats.MessageParser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandInfo implements CommandExecutor {

    private final AnarchyStats anarchyStats;
    private final MessageParser messageParser;

    public CommandInfo(AnarchyStats anarchyStats, MessageParser messageParser) {
        this.anarchyStats = anarchyStats;
        this.messageParser = messageParser;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(anarchyStats, anarchyStats::updateWorldSize);
        for (String x : messageParser.getCommandMessage()) {
            sender.sendMessage(x);
        }
        return true;
    }
}