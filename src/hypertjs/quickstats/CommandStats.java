package hypertjs.quickstats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStats implements CommandExecutor 
{	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Total Players: &b" + Bukkit.getOfflinePlayers().length + "&7."));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7World Size: &b" + WorldSize.humanReadableByteCount(WorldSize.getWorldSize(WorldSize.world, WorldSize.nether, WorldSize.end), true) + "&7."));
		return true;
	}

}