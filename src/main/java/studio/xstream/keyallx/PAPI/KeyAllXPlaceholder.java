package studio.xstream.keyallx.PAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.xstream.keyallx.KeyAllX;

import java.util.concurrent.TimeUnit;

public class KeyAllXPlaceholder extends PlaceholderExpansion {

    private final KeyAllX plugin;

    public KeyAllXPlaceholder(KeyAllX plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "keyallx";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("timer")) {
            long timerIntervalInSeconds = plugin.timeInterval;
            long days = TimeUnit.SECONDS.toDays(timerIntervalInSeconds);
            long hours = TimeUnit.SECONDS.toHours(timerIntervalInSeconds) % 24;
            long minutes = TimeUnit.SECONDS.toMinutes(timerIntervalInSeconds) % 60;
            long seconds = timerIntervalInSeconds % 60;

            if (days > 0) {
                return String.format("%d:%02d:%02d:%02d", days, hours, minutes, seconds);
            } else if (hours > 0) {
                return String.format("%02d:%02d:%02d", hours, minutes, seconds);
            } else if (minutes > 0) {
                return String.format("%02d:%02d", minutes, seconds);
            } else {
                return String.format("%02d", seconds);
            }
        }
        return null;
    }
}
