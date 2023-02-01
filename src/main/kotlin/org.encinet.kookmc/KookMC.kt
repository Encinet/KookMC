package org.encinet.kookmc

import org.bukkit.plugin.java.JavaPlugin
import kotlin.text.*
class KookMC : JavaPlugin() {
    companion object {
        lateinit var core: Core
    }
    override fun onEnable() {
        logger.fine(
            """
  _  __           _    __  __  _____ 
 | |/ /          | |  |  \/  |/ ____|
 | ' / ___   ___ | | _| \  / | |     
 |  < / _ \ / _ \| |/ / |\/| | |     
 | . \ (_) | (_) |   <| |  | | |____ 
 |_|\_\___/ \___/|_|\_\_|  |_|\_____|
 ------
 插件版本: ${this.description.version}
 插件作者: 繁空工作室
        """.trimIndent()
        )

        logger.info("加载Config...")
        this.reloadConfig()
        Config.load(this.config)

        logger.info("登录机器人...")
        core = Core("1/MTAyOTQ=/I/CpQpX0RC5QVfLVViKrUw==")
        core.register()

        logger.info("插件启动成功")
    }

    override fun onDisable() {
        logger.info("插件卸载成功")
    }
}