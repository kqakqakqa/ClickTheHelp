package cc.mcii.ClickTheHelp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import cc.mcii.ClickTheHelp.ClickTheHelp;

public class ConfigCommands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }
        if ("reload".equals(args[0])) {
            ClickTheHelp instance = ClickTheHelp.getInstance();
            instance.saveDefaultConfig();
            instance.reloadConfig();
            if (sender instanceof Player) {
                sender.sendMessage(ClickTheHelp.toConsoleMessage("&aReloaded!"));
            }
            instance.printToConsole("&aReloaded!");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<String>();
        if (args.length == 0 || args.length == 1) {
            list.add("reload");
        }
        return list;
    }
}
