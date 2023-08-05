package xyz.starsoc;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import xyz.starsoc.Event.command;
import xyz.starsoc.Event.groupMsg;
import xyz.starsoc.File.*;

public final class CustomPic extends JavaPlugin{
    public static final CustomPic INSTANCE=new CustomPic();
    public static final String path = CustomPic.INSTANCE.getDataHolderName();
    public static imageUtil imageUtil;
    public static int files;
    private CustomPic(){
        super(new JvmPluginDescriptionBuilder("xyz.starsoc.customPic","0.1.2")
                .name("customPic")
                .author("Clusters_stars")
                .build());
    }
    @Override
    public void onEnable(){
        reload();
        imageUtil = new imageUtil(config.INSTANCE.getImagePath());
        System.out.println("=========customPic==========");
        System.out.println("启动成功，图片目录创建在" + "\ndata/" + path + "/Pic" + "目录下");
        System.out.println("当前版本 0.1.2");
        CommandManager.INSTANCE.registerCommand(command.INSTANCE,true);
        GlobalEventChannel.INSTANCE.registerListenerHost(new groupMsg());
    }
    public void reload(){
       reloadPluginConfig(config.INSTANCE);
       reloadPluginConfig(message.INSTANCE);
       reloadPluginData(imageList.INSTANCE);
       reloadPluginData(imageData.INSTANCE);
    }
}