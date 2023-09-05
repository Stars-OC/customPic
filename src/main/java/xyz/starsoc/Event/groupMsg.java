package xyz.starsoc.Event;

import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;
import xyz.starsoc.CustomPic;
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
    private Map<Long,fileOperationer> files = new HashMap<>();
    private Map<Integer,String> url = Collections.synchronizedMap(new TreeMap<>());
    private message messageConfig = message.INSTANCE;
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        System.out.println("error1 :" + context);
        System.out.println("error2 :" + exception.getMessage());
        Bot bot = Bot.getInstance(config.getBot());
        bot.getFriend(config.getMaster()).sendMessage("customPic插件出现报错，请检查后台信息");
        // 处理事件处理时抛出的异常
    }

    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception {// 可以抛出任何异常, 将在 handleException 处理
        Long groupID = event.getGroup().getId();
        Long userID = event.getSender().getId();
        if(!groups.contains(groupID)){
            return;
        }

        String userKey = groupID + ":" + userID;
        String plain = null;
        Image image = null;
        String imageId = null;
        int id = 0;
        //event.getMessage().get(QuoteReply.Key).getSource().getOriginalMessage().get(Image.Key);
        for (SingleMessage message:  event.getMessage()){
            if (message instanceof PlainText){
                plain = ((PlainText) message).getContent();
            }else if(message instanceof Image){
                //将支持引用回复
                imageId = ((Image) message).getImageId();
                image = Image.fromId(imageId);
                //System.out.println(Image.queryUrl(image));
                //System.out.println(event.getSource().getIds()[0]);
            }else if(message instanceof QuoteReply){
                QuoteReply reply = (QuoteReply) message;
                MessageChain chain = reply.getSource().getOriginalMessage();
                if(chain.contentToString().contains("[图片]")){
                    id = reply.getSource().getIds()[0];
                }
               //System.out.println(reply.getSource().getIds()[0]);
            }
            //后续用list支持回复消息
        }
        if(image != null){
            addUrl(event.getSource().getIds()[0],imageId);
        }
        //解决Map存储对象过多的问题
        if(plain == null){
            user.remove(userKey);
            return;
        }
        //优化file创建的时机
        if(files.containsKey(groupID)){
            file = files.get(groupID);
        }else {
            file = new fileOperationer(event.getBot(),groupID);
            ++CustomPic.files;
            files.put(groupID,file);
        }
        //getPic
        String prefix = judgePrefix(plain);
        if (prefix != null){
            String tag = getSuffix(prefix,plain);
            if(!extended.containsKey(groupID) || !extended.get(groupID).contains(tag)){
                user.remove(userKey);
                return;
            }
            getPic(tag);
            //get成功才会将user放进去
            user.put(userKey,tag);
            return;
        }

//        if(true){
//            return;
//        }
        if(user.containsKey(userKey)){
            String userValue = user.get(userKey);
            if(againCMD.contains(plain)){
                getPic(userValue);
                return;
            }
        }
        user.remove(userKey);
        if(!plain.startsWith("pic ")){
            return;
        }
        if(plain.equals("pic help")){
            event.getGroup().sendMessage(messageConfig.getHelp());
            return;
        }
        if(!permission.contains(userKey)){
            //增加最高权限通过验证
            if(config.getMaster() != userID){
                event.getGroup().sendMessage(messageConfig.getNoPEX());
                return;
            }
        }

        if(plain.startsWith("pic down ")){
            if(id != 0){
                String url = getUrl(id);
                if(url == null){
                    event.getGroup().sendMessage(messageConfig.getNotGetUrl());
                    return;
                }
                down(plain,Image.fromId(url));
            }else {
                down(plain,image);
            }
            if(extended.containsKey(groupID) && !extended.get(groupID).contains(plain)){
                extended.get(groupID).add(plain);
            }
            return;
        }
        CMD(getSuffix("pic ",plain));
        user.remove(userKey);
    }
    private boolean down(String plain,Image image){
        if(image != null){
            downPic(getSuffix("pic down ",plain),Image.queryUrl(image));
        }else {
            send.sendText(messageConfig.getNoImage(),file);
        }
        return true;
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
            case "debug":
                System.out.println("========fileOperationer=======");
                System.out.println("对象创建:" + fileOperationer.getCount() + "次");
                System.out.println("对象剩余:" + CustomPic.files + "次");
                System.out.println("========user - map========");
                System.out.println("user剩余:" + user.size());
                System.out.println("user Info:" + user.toString());
                System.out.println("========files - map========");
                System.out.println("files剩余:" + files.size());
                System.out.println("files Info:" + files.toString());
                System.out.println("========url - map========");
                System.out.println("url剩余:" + url.size());
                System.out.println("url Info:" + url.toString());
                return;
            case "clear":
                user.clear();
                files.clear();
                url.clear();
                System.out.println("清除完成，所有内存已被清除");
                return;
            default:
                if(command.length < 2) {
                    send.sendText(messageConfig.getErrorCMD(), file);
                    return;
                }
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
        String msg = file.downPic(tag.replace(" ","").replace("\n",""), url);
        send.sendText(msg,file);
    }
    private void addUrl(int id,String image){
        if(url.size() >= config.getMapSize()){
            url.remove(url.keySet().stream().findFirst().orElse(null));
        }
        url.put(id,image);
    }
    private String getUrl(int id){
        if(!url.containsKey(id)){
            return null;
        }
        String image = url.get(id);
        url.remove(id);
        return image;
    }
}
