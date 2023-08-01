package xyz.starsoc.File

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object imageList : AutoSavePluginData("imageList"){
    val list : Set<String> by value()

}