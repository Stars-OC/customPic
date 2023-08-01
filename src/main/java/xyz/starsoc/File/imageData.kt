package xyz.starsoc.File


import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object imageData : AutoSavePluginData("imageData") {
    @ValueDescription("存储tag对应的图片")
    val list : Map<String,List<String>> by value()
    @ValueDescription("存储群拥有的tag\n只能存储tag")
    val extended : Map<Long,Set<String>> by value()
}