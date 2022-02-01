package cc.mcii.Helper;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener {
    private static final String HELP_KEY = "help";
    private static final String PLAYER_NAME_REGEX = "\\{player}";
    private static final String LIST_KEY = "list";
    private FileConfiguration fileConfiguration;

    enum ActionTypeEnum {
        CONSOLE(0, "打印文本"),
        RUN_COMMAND(1, "执行命令"),
        SUGGEST_COMMAND(2, "建议命令"),
        OPEN_URL(3, "打开网址"),
        OPEN_FILE(4, "打开文件"),
        CHAT(5, "发送聊天");

        private final int code;
        private final String description;

        ActionTypeEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return this.code;
        }
    }

    @Override
    public void onEnable() {
        this.getFileConfiguration();
        printMyInfo();
    }

    @Override
    public void onDisable() {
        printColorPluginMessage("&c已卸载");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("&7[&b帮助信息&7]>>&c帮助信息不能在后台展示！");
            return true;
        }
        Player player = (Player) sender;
        // 插件指令
        if (commandLabel.equals("helper")) {
            this.helper(player, args);
            return true;
        }
        // help指令
        if (args.length == 0) {
            printList(player, HELP_KEY);
        } else if (args.length == 1) {
            printList(player, args[0]);
        } else {
            printList(player, HELP_KEY);
        }
        return true;
    }

    private FileConfiguration getFileConfiguration() {
        if (this.fileConfiguration == null) {
            super.saveResource("config.yml", false);
            this.fileConfiguration = super.getConfig();
        }
        return this.fileConfiguration;
    }

    private void printMyInfo() {
        printColorPluginMessage("============ [HelperListBean——帮助信息] ============");
        printColorPluginMessage("感谢使用帮助信息    HelperListBean");
        printColorPluginMessage("作者：冷枫小乐 QQ: 2242299685");
        printColorPluginMessage("MCBBS论坛ID：冷枫小乐");
        printColorPluginMessage("如有问题，可以加群，370383209");
        printColorPluginMessage("============ [HelperListBean——帮助信息] ============");
    }

    private void printColorPluginMessage(String msg) {
        printColorMessage("&7[&bHelper&7]&a" + msg);
    }

    private void printColorMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    private String getFinalMessage(String message, String playerName) {
        if (playerName != null && !"".equals(playerName)) {
            message = message.replaceAll(PLAYER_NAME_REGEX, playerName);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void helper(Player player, String[] args) {
        if (args.length == 0) {
            return;
        }
        if ("reload".equals(args[0]) && player.isOp()) {
            reloadConfig();
            player.sendMessage(getFinalMessage("&7[&b帮助信息&7]>>&a重载成功!", player.getName()));
        }
        if ("print".equals(args[0]) && args.length > 1) {
            String content = "";
            for (int i = 0; i < args.length; i++) {
                if (i != 0) {
                    content += args[i] + " ";
                }
            }
            player.sendMessage(getFinalMessage(content.trim(), player.getName()));
        }
    }

    private void printList(Player player, String path) {
        getLogger().log(Level.INFO, path);
        List<Map<?, ?>> mapList = getFileConfiguration().getMapList(path + "." + LIST_KEY);
        if (mapList.size() != 0) {
            for (Map<?, ?> map : mapList) {
                HelperBean helperBean = new HelperBean(map);
                TextComponent textComponent = new TextComponent();
                String content = getFinalMessage(helperBean.getContent(), player.getName());
                textComponent.setText(getFinalMessage(helperBean.getText(), player.getName()));
                if (helperBean.getLore() != null && !"".equals(helperBean.getLore())) {
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(getFinalMessage(helperBean.getLore(), player.getName()))).create()));
                }
                if (ActionTypeEnum.RUN_COMMAND.getCode().equals(helperBean.getType())) {
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, content));
                } else if (ActionTypeEnum.SUGGEST_COMMAND.getCode().equals(helperBean.getType())) {
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, content));
                } else if (ActionTypeEnum.OPEN_URL.getCode().equals(helperBean.getType())) {
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, content));
                } else if (ActionTypeEnum.OPEN_FILE.getCode().equals(helperBean.getType())) {
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, content));
                } else if (ActionTypeEnum.CHAT.getCode().equals(helperBean.getType())) {
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/helper print " + helperBean.getContent()));
                }
                if (player.spigot() != null) {
                    player.spigot().sendMessage(textComponent);
                } else {
                    player.sendMessage(textComponent.getText());
                }
            }
        } else {
            player.sendMessage("&7没有该指令的说明");
        }
    }
}
