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
    val notGetUrl by value("获取失败，图片已失效")

    val successedAdd by value("添加 %tag% 成功")
    val notAdd by value("添加 %tag% 失败")
    val successedDelete by value("从群聊 %group% 删除 %tag% 成功")
    val notDelete by value("从群聊 %group% 删除 %tag% 失败")
    val successedExtendTo by value("群聊 %group% 添加 %tag% 成功")
    val notExtendTo by value("群聊 %group% 添加 %tag% 失败")
    val notExtend by value("群聊 %group% 删除 %tag% 成功")

    val help by value("====help====" +
            "\npic get tag(要设置的标签名称) 或者来张tag" +
            "\n可以获取该tag下的图片 eg来张test" +
            "\npic down tag(要设置的标签名称) 图片" +
            "\n下载一个图片并放在tag中" +
            "\npic addTag tag(要设置的标签名称) " +
            "\n初始化Tag，将tag进行创建" +
            "\npic deleteTag tag(要设置的标签名称)" +
            "\n删除tag，该tag不会被触发" +
            "\npic extendTo tag(要设置的标签名称)" +
            "\n将tag放在使用命令的群下，该群可以利用" +
            "\nprefix前缀 加 tag 来获取tag下图片" +
            "\n只有用这个命令并绑定已有tag才可以在群内用get指令" +
            "\npic notExtend tag(要设置的标签名称)" +
            "\n将该tag移除当前群" +
            "\n管理的相关命令请看原帖，在这不做多概述")
    val successedReload by value("customPic重载成功")
    val Maker by value("%maker% %user% %status%")
}
