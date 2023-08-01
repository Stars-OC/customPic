package xyz.starsoc.Message;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import xyz.starsoc.File.fileOperationer;

public class send {

    public static void sendText(String message, fileOperationer file) {
        Group group = file.getBot().getGroup(file.getGroup());
        group.sendMessage(message);
    }
    public static void sendImage(Image image,fileOperationer file){
        Group group = file.getBot().getGroup(file.getGroup());
        group.sendMessage(image);
    }
    public static void sendMix(Image image,String message,fileOperationer file){
        Group group = file.getBot().getGroup(file.getGroup());
        group.sendMessage(image.plus(message));

    }
    public static void sendTagText(String message,String tag,fileOperationer file){
        sendText(message.replace("%tag%",tag).replace("%group%",file.getGroup()+""),file);
    }
}
