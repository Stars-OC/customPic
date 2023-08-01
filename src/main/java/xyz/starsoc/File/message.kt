package xyz.starsoc.File

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object message : AutoSavePluginConfig("message") {
    val initGroup by value("该群还没有对应的图片")
    val noTag by value("没有对应的Tag,请添加")
    val noImage by value("图片不存在")
    val noTagList by value("该Tag不存在")
    val noUrl by value("找不到图片地址")
    val noNet by value("下载失败，网络出现问题")
    val hadTag by value("该群已有该Tag")
    val successedDown by value("添加成功")
    val noPEX by value("没有权限")
    val errorCMD by value("命名参数不对，请检查参数")

    val successedAdd by value("添加 %tag% 成功")
    val notAdd by value("添加 %tag% 失败")
    val successedDelete by value("从群聊 %group% 删除 %tag% 成功")
    val notDelete by value("从群聊 %group% 删除 %tag% 失败")
    val successedExtendTo by value("群聊 %group% 添加 %tag% 成功")
    val notExtendTo by value("群聊 %group% 添加 %tag% 失败")
    val notExtend by value("群聊 %group% 删除 %tag% 成功")

    val help by value("====help====\n")
    val successedReload by value("customPic重载成功")
    val Maker by value("%maker% %user% %status%")
}
