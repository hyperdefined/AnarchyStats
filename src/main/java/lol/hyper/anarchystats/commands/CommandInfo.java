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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandInfo extends AbstractCommand {

    private final AnarchyStats anarchyStats;

    public CommandInfo(String command, AnarchyStats anarchyStats) {
        super(command);
        this.anarchyStats = anarchyStats;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (anarchyStats.config.getBoolean("use-permission-node")) {
            String permission = anarchyStats.config.getString("permission-node");
            if (permission == null) {
                sender.sendMessage(Component.text("Permission node is not set for this command! Please see 'permission-node' under AnarchyStats' config!").color(NamedTextColor.RED));
                return true;
            }
            if (!sender.hasPermission(permission)) {
                sender.sendMessage(Component.text("You do not have permission for this command.").color(NamedTextColor.RED));
                return true;
            }
        }
        Bukkit.getAsyncScheduler().runNow(anarchyStats, scheduledTask -> anarchyStats.updateWorldSize());
        Component infoCommand = anarchyStats.messageParser.infoCommand();
        if (infoCommand != null) {
            sender.sendMessage(infoCommand);
        }
        return true;
    }
}
