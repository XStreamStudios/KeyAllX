package studio.xstream.keyallx.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.xstream.keyallx.KeyAllX;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabCompleter {
    private final KeyAllX plugin;

    public ReloadCommand(KeyAllX plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {

        if(command.getName().equalsIgnoreCase("keyallx") && args.length > 0 && args[0].equalsIgnoreCase("reload")) {

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("reload");
        }
        return completions;
    }
}