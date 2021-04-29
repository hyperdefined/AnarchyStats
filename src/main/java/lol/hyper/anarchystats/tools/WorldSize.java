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

import org.bukkit.Bukkit;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class WorldSize {

    /**
     * Attempts to calculate the size of a file or directory.
     * Since the operation is non-atomic, the returned value may be inaccurate.
     * However, this method is quick and does its best.
     * <p>
     * https://stackoverflow.com/a/19877372
     */
    public static long getWorldSize(ArrayList < Path > paths) {

        final AtomicLong size = new AtomicLong(0);

        for (Path p: paths) {
            try {
                Files.walkFileTree(p, new SimpleFileVisitor < Path > () {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                        size.addAndGet(attrs.size());
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) {

                        Bukkit.getLogger().warning("File " + file + " doesn't exist. (" + exc + ")");
                        // Skip folders that can't be traversed
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

                        if (exc != null)
                            Bukkit.getLogger().warning("Had trouble traversing: " + dir + " (" + exc + ")");
                        // Ignore errors traversing a folder
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
            }
        }

        return size.get();
    }

    /**
     * https://stackoverflow.com/a/5599842
     */
    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[] {
                "B",
                "kB",
                "MB",
                "GB",
                "TB"
        };
        int digitGroups = (int)(Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}