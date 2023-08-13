# customPic
本项目是基于[Mirai](https://github.com/mamoe/mirai)框架开发的一个利用于MCL的插件

可以用来收集图片并通过对应的**Tag**进行发送 **高度自定义** 


## 效果图
![get](/READMEPic/获取图片.png)

![again](/READMEPic/再来一张.png)

![help](/READMEPic/help.png)

![down](/READMEPic/down.png)

![helpCMD](/READMEPic/helpCMD.png)
## 指令
### 分为两种指令集
一种是利用拥有权限的指令集的方法，需要引入[chat-Command](https://github.com/project-mirai/chat-command)进行使用  
权限为`xyz.starsoc.custompic:*`利用`/permission add QQ(你的QQ号) xyz.starsoc.custompic:*`   
进行权限管理(*当然如果你有全部权限这个可以忽略不看*)    

`/pic reload` 重载插件的配置   
`/pic addGroup QQ群号` 用来添加启用本插件的QQ群  
`/pic delete QQ群号` 用来删除启用本插件的QQ群  
`/pic group at` 懒人指令，直接在要启用的QQ群输入这个指令，直接启用  

#### 这个就是另一个指令集的管理权限运用
`/pic addPer QQ号` 这个是给当前群该QQ使用另一个指令集的权限    
`/pic deletePer QQ号` 这个便是删除当前群该QQ使用另一个指令集的权限   
**注意这两个指令需要在要给权限的群里使用，否则会出现问题**可以进行at给权限

#### 另一个指令集的使用
`pic help`是一个很详细的部分指令集的查看
![helper](/READMEPic/help.png)  
    
当然也有其他指令集来查看更多信息，但是前提是拥有权限  
`pic listAllTag` 列出所有存在的tags，可以更好的进行管理  
`pic listTag` 列出在使用此命令的群聊拥有的tags    
`pic listAllPic` 列出所有存在的图片  
`pic listTagPic tag(要设置的标签名称)` 这个就是查看该tag下有多少张图片
`pic debug` 就是查看有多少占用的对象(debug的时候用 可以看看有多少占用)      
`pic clear` 将目前所有对象进行清除    

### 具体使用
```
对于添加权限
如果你有chat-command的全部权限的话直接跳过 
/permission add QQ(你的QQ号) xyz.starsoc.custompic:*
然后添加群聊/pic addGroup 1234(用1234代替群聊)
最后添加权限给在这个群聊的人/pic addPer 123456(在1234群聊中给123456加权限)

对于添加Tag到群下(下面用test代替tag)
pic addTag test 创建test的tag(其实用pic down可以直接创建加存储test)
pic down test 一张图片(加入图片  如果没有可以先跳过)
pic extendTo test 在要加test的群使用这个指令
然后使用pic get test 或者 来张test 就可以获取图片了
```
## 配置文件
主要的配置文件存在于`config\xyz.starsoc.customPic`中的`config`文件  
```
#启用的Bot，也就是如果出问题报错将发给谁
Bot: 123
# 最高权限
Master: 123456
#这个是对于图片缓存存放在哪里，如果不做设置默认存在data\xyz.starsoc.customPic文件里
imagePath: ''
# 存储多少历史图片
# 也就是如果设置多的话，以往消息就能够获取到图片进行下载
# 但是相对应的所占内存会变高
mapSize: 30
# 启用群聊(可以利用指令集进行更改)
Group: 
  - 1234
# 权限管理
# 也就是1234群的管理员是123456
permission: 
  - '1234:123456'
# 获取图片命令前缀(这个也就是获取图片的指令，如何进行获取图片)
prefixCMD: 
  - 来张
  - 'pic get '
# 再次获取桶tag图片命令(这个便是如果之前获取了图片，该如何再来一张的指令)
againCMD: 
  - 再来一张
  - again
```

## 后文
如果有什么问题可以点击左上角的[Issues](https://github.com/Stars-OC/customPic/issues)进行报告，如果看到我会及时处理    
如果有什么好的建议，也可以进行提交。

## 更新
`v0.2.0 customPic` 更新了可以使用QQ的**回复**功能进行下载图片     
`使用方法` 回复然后输入pic down tag即可 然后又添加了一个配置  
`mapSize` 这个就是能存多少张图片缓存 具体看上面的配置文件解析
