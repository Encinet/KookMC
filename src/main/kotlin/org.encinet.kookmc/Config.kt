package org.encinet.kookmc

import org.bukkit.configuration.file.FileConfiguration

class Config() {
    companion object {
        var numMessage: MutableMap<Int, List<String>> = HashMap()
        fun load(config: FileConfiguration) {
            val nums = config.getMapList("NumMessage")
            for (map in nums) {
                val num = map["num"] as Int
                val messages = map["text"] as List<String>
                numMessage[num] = messages
            }
        }
    }
}