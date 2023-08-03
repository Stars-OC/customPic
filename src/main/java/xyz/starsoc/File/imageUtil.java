package xyz.starsoc.File;

import xyz.starsoc.CustomPic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

public class imageUtil {
    private static String imagePath;
    private static File file;
    private static Set<String> list = imageList.INSTANCE.getList();
    public imageUtil(String imagePath){
        this.imagePath = imagePath;

        if(imagePath == null || imagePath == ""){
            this.imagePath = "data/" + CustomPic.path + "/Pic";
        }
        //CustomPic.imagePath = this.imagePath;
        createFile();
    }
    public Boolean addImage(String pic,String url) {
        if(pic == null){
            return false;
        }
        BufferedImage buf = null;
        File file = new File(imagePath + "/" +  pic + ".png");
        try {
            buf = ImageIO.read(new URL(url));
            if(buf != null){
                ImageIO.write(buf,"png",file);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        list.add(pic);
        return true;
    }

    public Boolean deleteImage(String pic) {
        if(pic == null || !list.contains(pic)){
            return false;
        }
        File file = new File(imagePath + "/" +  pic + ".png");
        list.remove(pic);
        if (!file.exists()){
            return false;
        }
        file.delete();
        return true;
    }
    public File getImage(String pic){
        if(pic == null ||!list.contains(pic)){
            return null;
        }
        File file = new File(imagePath + "/" +  pic + ".png");
        if(!file.exists()){
            list.remove(pic);
            return null;
        }
        return file;
    }
    public void createFile(){
        this.file = new File(imagePath);
        if(!file.exists()){
            System.out.println("文件夹不存在，正在创建文件夹");
            file.mkdir();
        }
//        System.out.println(file.getAbsolutePath());
    }
    public static String getPath() {
        return imagePath;
    }
}
