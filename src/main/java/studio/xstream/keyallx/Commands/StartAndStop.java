package studio.xstream.keyallx.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.xstream.keyallx.KeyAllX;

public class StartAndStop implements CommandExecutor {

    private final KeyAllX plugin;

    public StartAndStop(KeyAllX plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        if (cmd.equalsIgnoreCase("reset-timer")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("keyallx.reset_timer")) {
                    player.sendMessage("You don't have permissions to run this command");
                    return true;
                }
                plugin.stopTimer();
                plugin.startTimer();
                player.sendMessage(ChatColor.GREEN + "Timer has been reset!");
            } else {
                plugin.stopTimer();
                plugin.startTimer();
                sender.sendMessage("Timer has been reset!");
            }
        }
        return true;
    }
}