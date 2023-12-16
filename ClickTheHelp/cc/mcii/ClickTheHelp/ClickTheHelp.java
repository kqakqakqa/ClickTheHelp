package cc.mcii.ClickTheHelp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import cc.mcii.ClickTheHelp.commands.ConfigCommands;
import cc.mcii.ClickTheHelp.commands.HelpCommands;

public class ClickTheHelp extends JavaPlugin {
    private static ClickTheHelp instance;

    public static ClickTheHelp getInstance() {
        return instance;
    }

    public ClickTheHelp() {
    }

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        printPluginInfo();
        this.getCommand("clickthehelp").setExecutor(new ConfigCommands());
        this.getCommand("clickthehelp").setTabCompleter(new ConfigCommands());
        this.getCommand("help").setExecutor(new HelpCommands());
    }

    public void onDisable() {
        printToConsole("&c已卸载");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    private void printPluginInfo() {
        printToConsole("&a感谢使用ClickTheHelp");
        printToConsole("&a作者：冷枫小乐");
    }

    public void printToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(toConsoleMessage(message));
    }

    public static String toConsoleMessage(String message) {
        message = "&7[&bClickTheHelp&7]>> " + message;
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public void printToPlayer(String message, Player player) {
        player.sendMessage(toPlayerMessage(message, player.getName()));
    }

    public static String toPlayerMessage(String message, String playerName) {
        if (playerName != null && !"".equals(playerName))
            message = message.replaceAll("\\{player}", playerName);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }
}