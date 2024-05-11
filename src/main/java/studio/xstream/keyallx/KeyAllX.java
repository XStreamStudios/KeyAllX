package studio.xstream.keyallx;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import studio.xstream.keyallx.Commands.ReloadCommand;
import studio.xstream.keyallx.Commands.StartAndStop;
import studio.xstream.keyallx.PAPI.KeyAllXPlaceholder;

import java.io.File;
import java.util.List;

public final class KeyAllX extends JavaPlugin {

    public int timeInterval = 60;
    public int taskId;
    private ConsoleCommandSender console;

    @Override
    public void onEnable() {
        File configFile  = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getLogger().info("Creating config.yml...");
            saveDefaultConfig();
        }

        FileConfiguration config = getConfig();

        timeInterval = config.getInt("timeInterval", 60);

        saveConfig();

        console = Bukkit.getConsoleSender();

        getCommand("keyallx").setExecutor(new ReloadCommand(this));
        getCommand("keyallx").setTabCompleter(new ReloadCommand(this));
        getCommand("reset-timer").setExecutor(new StartAndStop(this));

        int pluginId = 21830;
        Metrics metrics = new Metrics(this, pluginId);
        getLogger().info("Loaded bStats");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new KeyAllXPlaceholder(this).register();
        } else {
            getLogger().warning("PlaceholderAPI not found! Placeholder will not work.");
        }

        startTimer();

    }

    @Override
    public void onDisable() {
        stopTimer();
    }

    // Timer  | Start & Stop |

    public void startTimer() {
        timeInterval = getConfig().getInt("timeInterval", 60);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            timeInterval--;
            if (timeInterval <= 0)  {
                for (Player player  : Bukkit.getOnlinePlayers()) {
                    if (player.isOnline()) {
                        //  play sound
                        if (getConfig().getBoolean("enable-sound-effects")) {
                            String soundName = getConfig().getString("sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
                            try {
                                Sound sound = Sound.valueOf(soundName);
                                float volume = (float) getConfig().getDouble("volume", 1.0);
                                float pitch = (float) getConfig().getDouble("pitch", 1.0);
                                player.playSound(player.getLocation(), sound, volume, pitch);
                            } catch (IllegalArgumentException e) {
                                getLogger().warning("Invalid sound specified in the configuration: " + soundName);
                            }
                        }


                        // Send title

                        if (getConfig().getBoolean("enable-title-message")) {
                            String titleMessage = getConfig().getString("title-message", "&aCongratulations!");
                            String subTitleMessage = getConfig().getString("subtitle-message", "You've been awarded a key as part of our Key All event!");

                            titleMessage = translateColorCodes(titleMessage);
                            subTitleMessage = translateColorCodes(subTitleMessage);

                            int fadeIn = getConfig().getInt("title-fade-in", 10);
                            int stay = getConfig().getInt("title-stay", 70);
                            int fadeOut = getConfig().getInt("title-fade-out", 20);

                            player.sendTitle(titleMessage, subTitleMessage, fadeIn, stay, fadeOut);
                        }

                        // Hotbar message
                        if (getConfig().getBoolean("enable-hotbar-message")) {
                            String hotbarmessage = getConfig().getString("hotbar-message");
                            hotbarmessage = translateColorCodes(hotbarmessage);

                            player.sendActionBar(hotbarmessage);
                        }

                        // Send message
                        List<String> messages = getConfig().getStringList("message");
                        if (messages.isEmpty()) {
                            messages.add("&aCongratulations! You've been awarded a key as part of our Key All event!");
                        }

                        for (String message :  messages) {
                            player.sendMessage(translateColorCodes(message));
                        }
                    }
                }

                // Execute the command
                Bukkit.getScheduler().runTask(this, () -> {
                    try {
                        final String command = getConfig().getString("command");
                        boolean debugEnabled = getConfig().getBoolean("debug", false);

                        if (debugEnabled) {
                            getLogger().info("Debug mode is enabled.");
                        }

                        if (debugEnabled) {
                            getLogger().info("Command to dispatch: " + command); // Log the command for debugging
                        }

                        if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                            if (debugEnabled) {
                                getLogger().info("Dispatching command to online players...");
                            }
                            Bukkit.dispatchCommand(getServer().getConsoleSender(), command);
                        } else {
                            if (debugEnabled) {
                                getLogger().info("No online players to dispatch command to.");
                            }
                        }
                    } catch (Exception e) {
                        getLogger().warning("Error while dispatching command: " + e.getMessage());
                        e.printStackTrace();
                    }
                });


                timeInterval = getConfig().getInt("timeInterval", 60);
            }
        }, 20L, 20L);
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void resetTimer() {
        stopTimer();
        startTimer();
    }

    private String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
