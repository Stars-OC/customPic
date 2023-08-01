package xyz.starsoc.Event;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import org.jetbrains.annotations.NotNull;
import xyz.starsoc.CustomPic;
import xyz.starsoc.File.config;
import xyz.starsoc.File.imageData;
import xyz.starsoc.File.message;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class command extends JCompositeCommand {
    public static final command INSTANCE = new command();
    private message message = xyz.starsoc.File.message.INSTANCE;
    private Set<Long> group = config.INSTANCE.getGroup();
    private imageData imgData = imageData.INSTANCE;
    private Set<String> per = config.INSTANCE.getPermission();
    private Map<Long, Set<String>> extended = imgData.getExtended();

    private command() {
        super(CustomPic.INSTANCE, "pic"); // 使用插件主类对象作为指令拥有者；设置主指令名为 "test"
        // 可选设置如下属性
        setDescription("自定义图片总命令行"); // 设置描述，也会在 /help 中展示
        //setPrefixOptional(true); // 设置指令前缀是可选的，即使用 `test` 也能执行指令而不需要 `/test`
    }

    @SubCommand("reload")
    public void reload(@NotNull CommandSender sender) {
        CustomPic.INSTANCE.reload();
        sender.getSubject().sendMessage(message.getSuccessedReload());
    }

    @SubCommand("addGroup")
    public void addGroup(@NotNull CommandSender sender, Long groupID) {
        Contact send = sender.getSubject();
        if (extended.containsKey(groupID)) {
            send.sendMessage(message.getMaker().replace("%user%", groupID + "").replace("%status%", "失败").replace("%maker%", "添加"));
            return;
        }
        extended.put(groupID, new HashSet<>());
        group.add(groupID);
        send.sendMessage(message.getMaker().replace("%user%", groupID + "").replace("%status%", "成功").replace("%maker%", "添加"));
    }

    @SubCommand("group at")
    public void addGroupThis(@NotNull CommandSender sender) {
        Contact send = sender.getSubject();
        if (!(send instanceof Group)) {
            send.sendMessage(message.getMaker().replace("%user%", "群聊").replace("%status%", "失败").replace("%maker%", "添加"));
            return;
        } else {
            addGroup(sender, send.getId());
        }
    }

    @SubCommand("deleteGroup")
    public void deleteGroup(@NotNull CommandSender sender, Long groupID) {
        Contact send = sender.getSubject();
        if (!extended.containsKey(groupID)) {
            send.sendMessage(message.getMaker().replace("%user%", groupID + "").replace("%status%", "失败").replace("%maker%", "删除"));
            return;
        }
        group.remove(groupID);
        extended.remove(groupID);
        send.sendMessage(message.getMaker().replace("%user%", groupID + "").replace("%status%", "成功").replace("%maker%", "删除"));
    }

    @SubCommand("addPer")
    public void addPer(@NotNull CommandSender sender, String u) {
        Contact send = sender.getSubject();
        Long user = Long.parseLong(u.replace("@", ""));
        if (!(send instanceof Group)) {
            send.sendMessage(message.getMaker().replace("%user%", "群聊中的 " + user).replace("%status%", "失败").replace("%maker%", "添加"));
            return;
        }
        Long group = send.getId();
        String key = group + ":" + user;
        String groupID = "群聊 " + group + " 中的 " + user;
        if (per.contains(key)) {
            send.sendMessage(message.getMaker().replace("%user%", groupID + "").replace("%status%", "失败").replace("%maker%", "添加"));
            return;
        }
        per.add(key);
        send.sendMessage(message.getMaker().replace("%user%", groupID + "").replace("%status%", "成功").replace("%maker%", "添加"));

    }
    @SubCommand("deletePer")
    public void deletePer(@NotNull CommandSender sender, String u) {
        Contact send = sender.getSubject();
        Long user = Long.parseLong(u.replace("@", ""));
        if (!(send instanceof Group)) {
            send.sendMessage(message.getMaker().replace("%user%", "群聊中的 " + user).replace("%status%", "失败,请在指定群聊中%maker%").replace("%maker%", "删除"));
            return;
        }
        Long group = send.getId();
        String key = group + ":" + user;
        String groupID = "群聊 " + group + " 中的 " + user;
        if (!per.contains(key)) {
            send.sendMessage(message.getMaker().replace("%user%", groupID + "").replace("%status%", "失败").replace("%maker%", "删除"));
            return;
        }
        per.add(key);
        send.sendMessage(message.getMaker().replace("%user%", groupID + "").replace("%status%", "成功").replace("%maker%", "删除"));

    }
}
