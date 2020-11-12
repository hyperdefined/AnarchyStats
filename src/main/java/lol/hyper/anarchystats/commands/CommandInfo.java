package lol.hyper.anarchystats.commands;

import lol.hyper.anarchystats.AnarchyStats;
import lol.hyper.anarchystats.WorldSize;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CommandInfo implements CommandExecutor {

    private final AnarchyStats anarchyStats;

    public CommandInfo(AnarchyStats anarchyStats) {
        this.anarchyStats = anarchyStats;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
        String date = anarchyStats.config.getString("date");
        LocalDate firstDay = LocalDate.parse(date, formatter);
        Bukkit.getScheduler().runTaskAsynchronously(anarchyStats, () -> anarchyStats.updateWorldSize(WorldSize.world, WorldSize.nether, WorldSize.end));
        sender.sendMessage(ChatColor.GOLD + "--------------------------------------------");
        sender.sendMessage(ChatColor.DARK_AQUA + anarchyStats.config.getString("server-name") + " was created on " + date + ". That was " + getDays() + " days ago.");
        sender.sendMessage(ChatColor.DARK_AQUA + "The world is " + AnarchyStats.worldSize + ".");
        sender.sendMessage(ChatColor.DARK_AQUA + "A total of " + Bukkit.getOfflinePlayers().length + " players have joined.");
        sender.sendMessage(ChatColor.GOLD + "--------------------------------------------");
        return true;
    }
    // Calculates the days between today and day 1.
    public long getDays() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
        String date = anarchyStats.config.getString("date");
        LocalDate firstDay = LocalDate.parse(date, formatter);
        return ChronoUnit.DAYS.between(firstDay, now);
    }
}