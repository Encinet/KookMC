package org.encinet.kookmc

import org.bukkit.configuration.file.FileConfiguration

class Config() {
    companion object {
        var numMessage: MutableMap<Int, List<String>> = HashMap()
        lateinit var targetID: List<String>
        lateinit var token: String
        fun load(config: FileConfiguration) {
            token = config.getString("token").toString()
            targetID = config.getStringList("targetID")
            val nums = config.getMapList("NumMessage")
            for (map in nums) {
                val num = map["num"] as Int
                val messages = map["text"] as List<String>
                numMessage[num] = messages
            }
        }
    }
}