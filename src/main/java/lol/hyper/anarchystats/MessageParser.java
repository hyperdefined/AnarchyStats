package lol.hyper.anarchystats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MessageParser {

    private final AnarchyStats anarchyStats;

    public MessageParser(AnarchyStats anarchyStats) {
        this.anarchyStats = anarchyStats;
    }

    public List<String> getCommandMessage() {
        String date = anarchyStats.config.getString("date");
        List<String> rawMessages = anarchyStats.config.getStringList("command-message");
        List<String> formattedMessage = new ArrayList<>();
        for (String x : rawMessages) {
            if (x.contains("{{STARTDATE}}")) {
                x = x.replace("{{STARTDATE}}", date);
            }

            if (x.contains("{{DAYS}}")) {
                x = x.replace("{{DAYS}}", Long.toString(getDays()));
            }

            if (x.contains("{{WORLDSIZE}}")) {
                x = x.replace("{{WORLDSIZE}}", AnarchyStats.worldSize);
            }

            if (x.contains("{{TOTALJOINS}}")) {
                x = x.replace("{{TOTALJOINS}}", Integer.toString(Bukkit.getOfflinePlayers().length));
            }

            formattedMessage.add(ChatColor.translateAlternateColorCodes('&', x));
        }

        return formattedMessage;
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