package xyz.starsoc.File;

import xyz.starsoc.CustomPic;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

public class imageUtil {
    private static String imagePath;
    private File file;
    private Set<String> list = imageList.INSTANCE.getList();
    private int interval = config.INSTANCE.getInterval();
    public imageUtil(String imagePath){
        this.imagePath = imagePath;

        if(imagePath == null || imagePath == ""){
            this.imagePath = "data/" + CustomPic.path + "/Pic";
        }
        //CustomPic.imagePath = this.imagePath;
        createFile();
    }
    public boolean addUrlImage(String pic,String url) {
        //将进行图片格式的自定义化
        if(pic == null){
            return false;
        }
        File file = new File(imagePath + "/" +  pic + ".png");
        try {
            BufferedImage buf = ImageIO.read(new URL(url));
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
    public int addUrlGIF(int num, String tag, String url){
        if(tag == null){
            return 0;
        }
        try {
            ImageInputStream inputStream = ImageIO.createImageInputStream(new URL(url).openStream());

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(inputStream);
            if (!imageReaders.hasNext()) {
                return 0;
            }
            ImageReader reader = imageReaders.next();
            reader.setInput(inputStream);
            int count = 0;
            while (true) {
                try {
                    ++count;
                    if(num%interval != 0){
                        continue;
                    }
                    BufferedImage frame = reader.read(num);

                    if (frame == null) {
                        break;
                    }
                    String pic = tag + "-" + num;
                    File file = new File(imagePath + "/" + pic + ".png");
                    ImageIO.write(frame, "PNG", file);
                    list.add(pic);
                    ++num;
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return num;
    }

    public boolean deleteImage(String pic) {
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
