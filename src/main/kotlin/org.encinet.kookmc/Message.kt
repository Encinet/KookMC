package org.encinet.kookmc

import com.github.hank9999.kook.Bot.Companion.kookApi
import com.github.hank9999.kook.types.Message
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.text.DateFormat
import java.util.*

class Message : Listener {
    @EventHandler
    //捏妈的，写个这玩意儿把我CPU干烧了
    suspend fun game(e: AsyncChatEvent) {
        val player: Player = e.player
        val msg: Component = e.message()
        if (msg.toString().first() == '#') {
            var msg1 = msg.toString()
            msg1 = msg1.substring(1)
            val message = player.name + ": " + msg1
            for (sendTarget in Config.targetID) {
                kookApi.message.create(sendTarget, message)
            }
        }
        /*val msg: Component = e.message()
        val player: Player = e.player
        if (msg.toString().first() == '#') {
            val message = player.name + ": " + e.message()
            val sendMessage = message.substring(0, message.length - 1)
            for (sendTarget in Config.targetID) {
                kookApi.message.create(sendTarget, sendMessage)
            }

        }*/
    }

    companion object {
        fun channel(msg: Message) {
            val extra: Message.Extra = msg.extra
            val senderName: String = extra.author.nickname

            if (extra.channelName == "游戏聊天互通") {
                val df: DateFormat = DateFormat.getDateInstance()
                // 时间格式化后的文本
                val time: String = df.format(Date(System.currentTimeMillis()))
                val textComponent: TextComponent = Component.text("")
                    .append(
                        Component.text("§8[§aKOOK§8]")
                            .hoverEvent(HoverEvent.showText(Component.text("§8| §b这是一条从KOOK发来的消息")))
                            .clickEvent(ClickEvent.suggestCommand("#"))
                    ).append(Component.text(senderName).color(NamedTextColor.WHITE))
                    .append(Component.text(": ").color(NamedTextColor.GRAY))
                    .append(Component.text(msg.content).hoverEvent(HoverEvent.showText(Component.text("时间 $time"))))
                Bukkit.getServer().sendMessage(textComponent)
            }
        }
    }
}