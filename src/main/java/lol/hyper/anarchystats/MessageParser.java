package lol.hyper.anarchystats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageParser {

    private final AnarchyStats anarchyStats;

    public MessageParser(AnarchyStats anarchyStats) {
        this.anarchyStats = anarchyStats;
    }

    public List<String> getCommandMessage() {
        String date = anarchyStats.config.getString("date");
        DateFormat originalFormat = new SimpleDateFormat("M/dd/yyyy", Locale.ENGLISH);
        DateFormat newFormat = new SimpleDateFormat(anarchyStats.config.getString("date-format"), Locale.ENGLISH);
        Date originalDate = null;
        try {
            originalDate = originalFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newDate = newFormat.format(originalDate);
        List<String> rawMessages = anarchyStats.config.getStringList("command-message");
        List<String> formattedMessage = new ArrayList<>();
        for (String x : rawMessages) {
            if (x.contains("{{STARTDATE}}")) {
                x = x.replace("{{STARTDATE}}", newDate);
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