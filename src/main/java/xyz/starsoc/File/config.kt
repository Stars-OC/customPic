package xyz.starsoc.File

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object config : AutoSavePluginConfig("config") {
    val Bot : Long by value()
    @ValueDescription("最高权限")
    val Master : Long by value()
    val imagePath by value("")
    @ValueDescription("存储多少历史图片\n也就是如果设置多的话，以往消息就能够获取到图片进行下载\n但是相对应的所占内存会变高")
    val mapSize by value(30)
    @ValueDescription("是否开启发送tag就出图片")
    val enableTag by value(true)
    @ValueDescription("存储抽帧后的GIF图进行随机化\n实验中...")
    val enableGIF by value(false)
    @ValueDescription("是否启用随机图片")
    val enableRandom by value(true)
    @ValueDescription("抽帧限制\n就是当是GIF分解的时候，多少帧抽一次")
    val interval by value(1)
    @ValueDescription("启用群聊")
    val Group : Set<Long> by value(mutableSetOf(1234))
    @ValueDescription("权限管理\n也就是1234群的管理员是123456")
    val permission : Set<String> by value(mutableSetOf("1234:123456"))
    @ValueDescription("获取图片命令前缀")
    val prefixCMD : Set<String> by value(mutableSetOf("来张","pic get "))
    @ValueDescription("再次获取同tag图片命令")
    val againCMD : Set<String> by value(mutableSetOf("再来一张","again"))
    @ValueDescription("获取随机图片的命令")
    val randomCMD : Set<String> by value(mutableSetOf("随机图片"))

}