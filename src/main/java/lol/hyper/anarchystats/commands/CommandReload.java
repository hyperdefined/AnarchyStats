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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class CommandReload implements TabExecutor {

    private final AnarchyStats anarchyStats;

    public CommandReload(AnarchyStats anarchyStats) {
        this.anarchyStats = anarchyStats;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "AnarchyStats version "
                    + anarchyStats.getDescription().getVersion() + ". Created by hyperdefined.");
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Collections.singletonList("reload");
    }
}
