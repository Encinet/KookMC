package org.encinet.kookmc

import com.github.hank9999.kook.Bot
import com.github.hank9999.kook.types.Message
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import java.text.DateFormat
import java.util.Date
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Collectors

class Command {
    @Bot.OnCommand("你在吗")
    suspend fun test(msg: Message) {
        msg.send("我在呢~")
    }

    @Bot.OnCommand("send", aliases = ["发送"])
    suspend fun send(msg: Message, content: String) {
        val sender: String = msg.authorId
        val df: DateFormat = DateFormat.getDateInstance()
        // 时间格式化后的文本
        val time: String = df.format(Date(System.currentTimeMillis()))
        val textComponent: TextComponent = Component.text("")
            .append(Component.text("§8[§eKOOK§8]").hoverEvent(
                HoverEvent.showText(Component.text("""
                                                §8| §b这是一条从KOOK发来的消息
                                                §8| §b使用#可互通
                                                §a➥ §b点击回复""")))
                .clickEvent(ClickEvent.suggestCommand("#")))
            .append(Component.text(sender).color(NamedTextColor.DARK_RED))
            .append(Component.text(": ").color(NamedTextColor.GRAY))
            .append(Component.text(content).hoverEvent(HoverEvent.showText(Component.text("Time $time"))))
        Bukkit.getServer().sendMessage(textComponent)
    }

    @Bot.OnCommand("list", aliases = ["在线"])
    suspend fun list(msg: Message) {
        var message: String
        // 添加玩家id列表
        var onlinePlayers: MutableList<String> = ArrayList()
        for (n in Bukkit.getServer().onlinePlayers) {
            onlinePlayers.add(n.name + if (n.isAfk) " [AFK]" else "")
        }
        // 字母顺序
        onlinePlayers = onlinePlayers.stream().sorted().collect(Collectors.toList())
        // 执行
        val num: Int = onlinePlayers.size
        if (Config.numMessage.containsKey(num)) {
            val messages: List<String> = Config.numMessage.get(num)!!
            message = messages[ThreadLocalRandom.current().nextInt(messages.size)] // 随机使用消息
            for (i in 0 until num) {
                message = message.replace("{$i}", onlinePlayers[i])
            }
        } else {
            message = "当前 $num 人在线 \n${java.lang.String.join("\n", onlinePlayers)}"
        }
        msg.send(message)
    }

    @Bot.OnCommand("banlist", aliases = ["封禁"])
    suspend fun banlist(msg: Message) {
        var bannedPlayers: MutableList<String> = ArrayList()
        for (n in Bukkit.getServer().bannedPlayers) {
            n.name?.let { bannedPlayers.add(it) }
        }
        bannedPlayers = bannedPlayers.stream().sorted().collect(Collectors.toList())
        val num: Int = bannedPlayers.size
        val message = "当前 $num 人被封禁 +\n${java.lang.String.join("\n", bannedPlayers)}"
        msg.send(message)
    }
}