package cc.mcii.ClickTheHelp.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.chat.*;

import cc.mcii.ClickTheHelp.ClickTheHelp;

public class HelpCommands implements CommandExecutor {
    private ClickTheHelp instance;
    private final String DEFAULT_PATH = "help";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.instance = ClickTheHelp.getInstance();
        if (!(sender instanceof Player)) {
            sender.sendMessage(ClickTheHelp.toConsoleMessage("&cYou must be a player to use this command!"));
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 1) {
            printHelpListToPlayer(player, args[0]);
            return true;
        }
        printHelpListToPlayer(player, DEFAULT_PATH);
        return true;
    }

    public void printHelpListToPlayer(Player player, String path) {
        this.instance.getLogger().log(Level.INFO, path);
        List<Map<?, ?>> mapList = this.instance.getConfig().getMapList(path);
        if (mapList.size() == 0) {
            if (!DEFAULT_PATH.equals(path)) {
                printHelpListToPlayer(player, DEFAULT_PATH);
            }
            return;
        }
        for (Map<?, ?> map : mapList) {
            Map<String, String> helpMap = getProcessedMap(map);
            TextComponent textComponent = getTextComponent(player, helpMap);
            if (player.spigot() != null) {
                player.spigot().sendMessage((BaseComponent) textComponent);
                continue;
            }
            player.sendMessage(textComponent.getText());
        }
    }

    private Map<String, String> getProcessedMap(Map<?, ?> mapRaw) {
        Map<String, String> map = new HashMap<String, String>();
        String type = "text";
        if (mapRaw.get("type") != null) {
            type = mapRaw.get("type").toString();
        }
        map.put("type", type);
        map.put("content", mapRaw.get("content").toString());
        map.put("lore", mapRaw.get("lore").toString());
        map.put("text", mapRaw.get("text").toString());
        return map;
    }

    private TextComponent getTextComponent(Player player, Map<String, String> map) {
        TextComponent textComponent = new TextComponent();
        textComponent.setText(ClickTheHelp.toPlayerMessage(map.get("text"), player.getName()));
        Integer versionNumber = getVersionNumber();
        if (map.get("lore") != null && !"".equals(map.get("lore")) && versionNumber != null) {
            String text = ClickTheHelp.toPlayerMessage(map.get("lore"), player.getName());
            if (versionNumber > 13) {
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(text)));
            } else {
                textComponent.setHoverEvent(
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(text).create()));
            }
        }
        String content = ClickTheHelp.toPlayerMessage(map.get("content"), player.getName());
        switch (map.get("type")) {
            case "run-command":
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, content));
                break;
            case "suggest-command":
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, content));
                break;
            case "open-url":
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, content));
                break;
            default:
                break;
        }
        return textComponent;
    }

    private Integer getVersionNumber() {
        String bukkitVersion = Bukkit.getVersion();
        String pattern = "\\(MC: [0-9]*\\.([0-9]*)(\\..*)*\\)";
        Matcher matcher = Pattern.compile(pattern).matcher(bukkitVersion);
        if (matcher.find()) {
            Integer versionNumber = Integer.valueOf(matcher.group(1));
            return versionNumber;
        }
        return null;
    }
}
