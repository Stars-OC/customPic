package xyz.starsoc.File;

import net.mamoe.mirai.Bot;
import xyz.starsoc.CustomPic;

import java.io.File;
import java.util.*;

public class fileOperationer {
    /**
     * 这一层只用管 imageConfig 文件的CRUD
     */

    private Long group;
    private imageData imgData = imageData.INSTANCE;
    private message message = xyz.starsoc.File.message.INSTANCE;
    private Map<String, List<String>> tagList = imgData.getList();

    private Map<Long, Set<String>> extended = imgData.getExtended();
    private Set<String> set;
    private imageUtil imageUtil = CustomPic.imageUtil;
    private Set<String> call = imgData.getCall();

    private File image;
    private Bot bot;
    private static int count;
    public fileOperationer(Bot bot, Long group) {
        this.bot = bot;
        this.group = group;
        if(!extended.containsKey(group)){
            extended.put(group,new HashSet<>());
        }
        ++count;
        set = extended.get(group);
    }

    public static int getCount() {
        return count;
    }

    public Long getGroup() {
        return group;
    }

    public Bot getBot() {
        return bot;
    }

    public File getImage() {
        return image;
    }

    private String randomPic(String tag){
        if(!tagList.containsKey(tag)){
            return null;
        }
        List<String> list = tagList.get(tag);
        return list.get((int)(Math.random()*list.size()));
    }
    private void initTagList(String tag){
        List<String> list = new ArrayList<>();
        list.add(tag + "-1");
        tagList.put(tag,list);
    }
    //好像这个方法可以直接用size :D
    private int getTagNum(List<String> arr){
        if(arr.size() == 0){
            return 0;
        }
        String[] st = arr.get(arr.size()-1).split("-");
        int a = Integer.parseInt(st[st.length-1]);
        return ++a;
    }
    private boolean addPic(String tag,String url){
        List<String> arr = tagList.get(tag);
        String image = tag + "-" + getTagNum(arr);
        boolean addUrlImage = imageUtil.addUrlImage(image, url);
        if(addUrlImage){
            arr.add(image);
        }
        return addUrlImage;
    }
    private boolean addGIF(String tag,String url){
        List<String> arr = tagList.get(tag);
        int old = getTagNum(arr);
        int num = imageUtil.addUrlGIF(old,tag,url);
        if(num == 0){
            return false;
        }
        for(int i = old;i < num;i++){
            arr.add(tag + "-" + i);
        }
        return true;
    }
    //获取文件里extended中的pic是否存在 耦合度太高了(看不下去)
    private String getPic(String tag,Boolean upload){
        if(!extended.containsKey(group)){
            extended.put(group, new HashSet<>());
            return message.getInitGroup();
        }
        if(!extended.get(group).contains(tag)){
            return message.getNoTag();
        }
        String randomPic = randomPic(tag);
        if(randomPic(tag) == null){
            return message.getNoTagList();
        }
        File image = imageUtil.getImage(randomPic);
        if(image == null){
            return message.getNoImage();
        }
        if(upload){
            this.image = image;
        }
        return "Yes";
    }
    public String uploadPic(String tag){
        String getPic = getPic(tag,true);
        return getPic;
    }
    public String downPic(String tag,String type,String url){
        if(!tagList.containsKey(tag) || tagList.get(tag).size() == 0){
            if("gif".equals(type)){
                tagList.put(tag,new ArrayList<>());
                addGIF(tag, url);
            }else{
                initTagList(tag);
                imageUtil.addUrlImage(tag + "-1",url);
            }
            return message.getSuccessedDown();
        }
        if(url == null || url == ""){
            return message.getNoUrl();
        }
        if("gif".equals(type)){
            if(!addGIF(tag, url)){
                return "error";
            }
            return message.getSuccessedAdd();
        }
        if(!addPic(tag,url)){
            return message.getNoNet();
        }
        return message.getSuccessedDown();
    }
    public Boolean addTag(String tag){
        if(tag == "" || tag == null){
            return false;
        }
        if(tagList.containsKey(tag)){
            return false;
        }
        tagList.put(tag,new ArrayList<>());
        return true;
    }
    public Boolean deleteTag(String tag){
        if(tagList.containsKey(tag)){
            tagList.remove(tag);
            return true;
        }
        return false;
    }
    public Boolean extendTo(String tag){
        if(!tagList.containsKey(tag)){
            return false;
        }
        set.add(tag);
        return true;
    }
    public String notExtend(String tag){
        if(!tagList.containsKey(tag)){
            return message.getNoTagList();
        }
        if(!set.contains(tag)){
            return message.getNoTag();
        }
        set.remove(tag);
        return "Yes";
    }
    public boolean callTo(String tag){
        if(!tagList.containsKey(tag)){
            return false;
        }
        call.add(tag);
        return true;
    }
    //暂时不写删除
    public void deletePic(){

    }
    //debug判断是否被回收
    @Override
    protected void finalize() throws Throwable {
        --CustomPic.files;
    }
}
