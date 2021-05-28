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

package lol.hyper.anarchystats.commands;

import lol.hyper.anarchystats.AnarchyStats;
import lol.hyper.anarchystats.tools.AbstractCommand;
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
