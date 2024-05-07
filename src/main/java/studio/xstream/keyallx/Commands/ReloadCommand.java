package studio.xstream.keyallx.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.xstream.keyallx.KeyAllX;

public class ReloadCommand implements CommandExecutor {
    private final KeyAllX plugin;

    public ReloadCommand(KeyAllX plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {

        if (cmd.equalsIgnoreCase("keyallx") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;
                if (!player.hasPermission("keyallx.reload")) {
                    player.sendMessage("You don't have permissions to run this command");
                    return true;
                }
                plugin.reloadConfig();
                player.sendMessage(ChatColor.GREEN + "Config has been reloaded!");
            } else {
                plugin.reloadConfig();
                sender.sendMessage("Config has been reloaded");
            }
        }
        return true;
    }


}