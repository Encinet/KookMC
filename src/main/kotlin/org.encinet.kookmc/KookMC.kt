package org.encinet.kookmc

import org.bukkit.plugin.java.JavaPlugin

class KookMC : JavaPlugin() {
    override fun onEnable() {
        logger.info("[KookMC]插件开始启动")
        logger.info("[KookMC]加载Config...")
        this.reloadConfig()
        Config.load(this.config)
        logger.info("登录机器人...")
        Core("1/MTAyOTQ=/os0GdVRlHLCiUEMIZWuxRA==").register()
        logger.info("[KookMC]插件启动成功")
    }

    override fun onDisable() {
        logger.info("[KookMC]插件卸载成功")
    }
}