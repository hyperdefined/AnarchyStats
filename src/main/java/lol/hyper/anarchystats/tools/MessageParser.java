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

    public MessageParser(AnarchyStats anarchyStats) {
        this.anarchyStats = anarchyStats;
    }

    /**
     * Builds the /info command from the config.
     *
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
            anarchyStats.logger.warn("The date in the config is invalid.");
            e.printStackTrace();
        }
        String finalDate;
        if (configFormat == null) {
            anarchyStats.logger.warn("date-format is invalid! Trying to use default formatting for date instead.");
            finalDate = originalFormat.format(originalDate);
        } else {
            finalFormat = new SimpleDateFormat(configFormat, Locale.ENGLISH);
            finalDate = finalFormat.format(originalDate);
        }

        List<String> rawMessages = anarchyStats.config.getStringList("command-message");
        if (rawMessages.isEmpty()) {
            anarchyStats.logger.warn("'command-message' is empty on the configuration!");
            return null;
        }

        for (int i = 0; i < rawMessages.size(); i++) {
            String line = rawMessages.get(i);

            line = line.replace("{{STARTDATE}}", finalDate);
            line = line.replace("{{DAYS}}", Long.toString(getDays()));
            line = line.replace("{{WORLDSIZE}}", anarchyStats.worldSize);
            line = line.replace("{{TOTALJOINS}}", Integer.toString(Bukkit.getOfflinePlayers().length));

            rawMessages.set(i, line);
        }

        return anarchyStats.textUtils.formatMultiLine(rawMessages);
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
