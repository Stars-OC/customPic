package xyz.starsoc.File

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import xyz.starsoc.File.config.provideDelegate

object config : AutoSavePluginConfig("config") {
    val Bot by value(2159165283)
    @ValueDescription("最高权限")
    val Master by value(1027715998)
    val imagePath by value("")
    @ValueDescription("启用群聊")
    val Group : Set<Long> by value(mutableSetOf(607402931))
    @ValueDescription("权限管理\n也就是607402931群的管理员是1027715998")
    val permission : Set<String> by value(mutableSetOf("607402931:1027715998"))
    @ValueDescription("获取图片命令前缀")
    val prefixCMD : Set<String> by value(mutableSetOf("来张","pic get "))
    @ValueDescription("再次获取桶tag图片命令")
    val againCMD : Set<String> by value(mutableSetOf("再来一张","again"))

}