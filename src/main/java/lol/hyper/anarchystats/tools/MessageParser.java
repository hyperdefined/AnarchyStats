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

package lol.hyper.anarchystats.tools;

import lol.hyper.anarchystats.AnarchyStats;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageParser {

    private final AnarchyStats anarchyStats;
    private final MiniMessage miniMessage;

    public MessageParser(AnarchyStats anarchyStats) {
        this.anarchyStats = anarchyStats;
        this.miniMessage = anarchyStats.miniMessage;
    }


    /**
     * Builds the /info command from the config.
     * @return A full component with the command.
     */
    public Component infoCommand() {
        String configDate = anarchyStats.config.getString("date");
        DateFormat originalFormat = new SimpleDateFormat("M/dd/yyyy", Locale.ENGLISH);
        String configFormat = anarchyStats.config.getString("date-format");
        DateFormat finalFormat;
        Date originalDate = null;
        try {
            originalDate = originalFormat.parse(configDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finalDate;
        if (configFormat == null) {
            anarchyStats.logger.severe("date-format is invalid! Trying to use default formatting for date instead.");
            finalDate = originalFormat.format(originalDate);
        } else {
            finalFormat = new SimpleDateFormat(configFormat, Locale.ENGLISH);
            finalDate = finalFormat.format(originalDate);
        }

        List<String> rawMessages = anarchyStats.config.getStringList("command-message");
        if (rawMessages.isEmpty()) {
            anarchyStats.logger.warning("'command-message' is empty on the configuration!");
            return null;
        }

        // start with an empty component
        Component infoCommand = Component.empty();
        for (int i = 0; i < rawMessages.size(); i++) {
            String line = rawMessages.get(i);
            if (line.contains("{{STARTDATE}}")) {
                line = line.replace("{{STARTDATE}}", finalDate);
            }

            if (line.contains("{{DAYS}}")) {
                line = line.replace("{{DAYS}}", Long.toString(getDays()));
            }

            if (line.contains("{{WORLDSIZE}}")) {
                line = line.replace("{{WORLDSIZE}}", anarchyStats.worldSize);
            }

            if (line.contains("{{TOTALJOINS}}")) {
                line = line.replace("{{TOTALJOINS}}", Integer.toString(Bukkit.getOfflinePlayers().length));
            }

            // append a new line + the component
            // don't add a new line if it's the first one
            // creates a gap
            if (i == 0) {
                infoCommand = miniMessage.deserialize(line);
            } else {
                infoCommand = infoCommand.append(Component.newline()).append(miniMessage.deserialize(line));
            }
        }
        return infoCommand;
    }

    // Calculates the days between today and day 1.
    public long getDays() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/dd/yyyy");
        String date = anarchyStats.config.getString("date");
        LocalDate firstDay = LocalDate.parse(date, formatter);
        return ChronoUnit.DAYS.between(firstDay, now);
    }
}
