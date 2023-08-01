package xyz.starsoc.Event;

import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;
import xyz.starsoc.File.*;
import xyz.starsoc.Message.send;

import java.util.*;

public class groupMsg extends SimpleListenerHost {
    private config config = xyz.starsoc.File.config.INSTANCE;
    private imageData imgData = imageData.INSTANCE;
    private Set<String> pics = imageList.INSTANCE.getList();
    private Map<Long, Set<String>> extended = imgData.getExtended();
    private fileOperationer file;
    private HashMap<String,String> user = new HashMap<>();
    private Set<Long> groups = config.getGroup();
    private Set<String> prefixCMD = config.getPrefixCMD();
    private Set<String> againCMD = config.getAgainCMD();
    private Set<String> permission = config.getPermission();
    private Map<String, List<String>> tagList = imgData.getList();
    private message messageConfig = message.INSTANCE;
//    @Override
//    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
//        System.out.println("error1 :" + context);
//        System.out.println("error2 :" + exception);
//        Bot bot = Bot.getInstance(config.getBot());
//        bot.getFriend(config.getMaster()).sendMessage("customPic插件出现报错，请检查后台信息");
//        // 处理事件处理时抛出的异常
//    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {// 可以抛出任何异常, 将在 handleException 处理
        Long groupID = event.getGroup().getId();
        Long userID = event.getSender().getId();
        if(!groups.contains(groupID)){
            return;
        }
        file = new fileOperationer(event.getBot(),groupID);
        String userKey = groupID + ":" + userID;
        String plain = null;
        Image image = null;
        for (SingleMessage message:  event.getMessage()){
            if (message instanceof PlainText){
                plain = ((PlainText) message).getContent();
            }else if(message instanceof Image){
                image = Image.fromId(((Image) message).getImageId());
            }
            //后续用list支持回复消息
        }

        if(!user.containsKey(userKey)){
            user.put(userKey,plain);
        }
        if(plain == null){
            user.put(userKey,plain);
            return;
        }
        //getPic
        String prefix = judgePrefix(plain);
        if (prefix != null){
            String tag = getSuffix(prefix,plain);
            if(!extended.containsKey(groupID) || !extended.get(groupID).contains(tag)){
                user.put(userKey,plain);
                return;
            }
            getPic(tag);
            user.put(userKey,tag);
            return;
        }
//        if(true){
//            return;
//        }

        String userValue = user.get(userKey);
        if(againCMD.contains(plain)){
            if(!tagList.containsKey(userValue)){
                return;
            }
            getPic(userValue);
            return;
        }
        user.put(userKey,plain);
        if(!plain.startsWith("pic ")){
            return;
        }
        if(plain.equals("pic help")){
            event.getGroup().sendMessage(messageConfig.getHelp());
            return;
        }
        if(!permission.contains(userKey)){
            event.getGroup().sendMessage(messageConfig.getNoPEX());
            return;
        }
        if(plain.startsWith("pic down ")){
            if(image != null){
                downPic(getSuffix("pic down ",plain),Image.queryUrl(image));
            }else {
                event.getGroup().sendMessage(messageConfig.getNoImage());
            }
            return;
        }
        CMD(getSuffix("pic ",plain));
    }
    private void CMD(String cmd){
        String[] command = cmd.split(" ");
        if (command.length < 1){
            return;
        }
        switch (command[0]){
            case "listAllTag":
                String listAllTags = "[";
                for(String tag : tagList.keySet()){
                    listAllTags += tag + ",";
                }
                listAllTags += "...]";
                send.sendText(listAllTags,file);
                return;
            case "listTag":
                String listTags = "[";
                for(String tag : extended.get(file.getGroup())){
                    listTags += tag + ",";
                }
                listTags += "...]";
                send.sendText(listTags,file);
                return;
            case "listAllPic":
                String listAllPic = "[";
                for(String tag : pics){
                    listAllPic += tag + ",";
                }
                listAllPic += "...]";
                send.sendText(listAllPic,file);
                return;
            default:
                if(command.length < 2) {
                    send.sendText(messageConfig.getErrorCMD(), file);
                    return;
                }
        }
        if(command.length < 2){
            send.sendText(messageConfig.getErrorCMD(),file);
            return;
        }
        String tag = command[1];
        switch (command[0]){
            case "addTag":
                if(file.addTag(tag)){
                    send.sendTagText(messageConfig.getSuccessedAdd(),tag,file);
                }else {
                    send.sendTagText(messageConfig.getNotAdd(),tag,file);
                }
                return;
            case "deleteTag":
                if(file.deleteTag(tag)){
                    send.sendTagText(messageConfig.getSuccessedAdd(),tag,file);
                }else {
                    send.sendTagText(messageConfig.getNotAdd(),tag,file);
                }
                return;
            case "extendTo":
                if(file.extendTo(tag)){
                    send.sendTagText(messageConfig.getSuccessedExtendTo(),tag,file);
                }else {
                    send.sendTagText(messageConfig.getNotExtendTo(),tag,file);
                }
                return;
            case "notExtend":
                String msg = file.notExtend(tag);
                if (msg.equals("Yes")){
                    send.sendTagText(messageConfig.getNotExtend(),tag,file);
                }else {
                    send.sendText(msg,file);
                }
                return;
            case "listTagPic":
                if(!tagList.containsKey(tag)){
                    send.sendText(messageConfig.getNoTag(), file);
                    return;
                }
                String listTagPic = "[";
                for(String pic : tagList.get(tag)){
                    listTagPic += pic + ",";
                }
                listTagPic += "...]";
                send.sendText(listTagPic,file);
                return;
            default:
                send.sendText(messageConfig.getErrorCMD(), file);

        }
    }
    private String getSuffix(String prefix,String plain){
        return plain.substring(prefix.length());
    }
    private void judgeTag(String tag){

    }
    private String judgePrefix(String msg){
        for (String prefix : prefixCMD){
            if(msg.startsWith(prefix)){
                return prefix;
            }
        }
        return null;
    }
    private void getPic(String tag){
        String msg = file.uploadPic(tag);
        if(!msg.equals("Yes")){
            send.sendText(msg,file);
            return;
        }
        Image image = file.getBot().getGroup(file.getGroup()).uploadImage(ExternalResource.create(file.getImage()).toAutoCloseable());
        send.sendImage(image,file);
    }
    private void downPic(String tag,String url) {
        if (tag == null||tag == ""){
            send.sendTagText(messageConfig.getNoTag(),tag,file);
            return;
        }
        String msg = file.downPic(tag.replace(" ",""), url);
        send.sendText(msg,file);
    }
}
