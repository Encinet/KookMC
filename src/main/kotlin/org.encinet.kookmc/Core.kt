package org.encinet.kookmc

import com.github.hank9999.kook.Bot
import com.github.hank9999.kook.Config

class Core(token: String) {
    private var bot: Bot = Bot(Config(token = token))

    fun register() {
        bot.registerClass(Command())
    }
}