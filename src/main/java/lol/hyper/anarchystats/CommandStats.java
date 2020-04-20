package lol.hyper.anarchystats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(AnarchyStats.getInstance(), () -> AnarchyStats.getInstance().getWorldSize());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b-------------------------------\n&7Total Players: &b" + Bukkit.getOfflinePlayers().length + "&7.\n&7World Size: &b" + AnarchyStats.getInstance().worldSize + "&7.\n&b-------------------------------"));
        return true;
    }
}