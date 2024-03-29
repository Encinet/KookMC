package org.encinet.kookmc

import com.github.hank9999.kook.Bot
import com.github.hank9999.kook.types.Message
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.encinet.kookmc.util.NumProcess
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Collectors


class Command : JavaPlugin() {
    @Bot.OnCommand("bind", aliases = ["绑定"])
    suspend fun bind(msg: Message) {
        val msg1 = msg.toString()
        val args = msg1.split(" ")
        val value = args[1]
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

    @Bot.OnCommand("info", aliases = ["状态"])
    suspend fun info(msg: Message) {
        val sb = StringBuilder()
        sb.append("服务器版本: ").append(Bukkit.getVersion())
            .append(String.format(" 在线玩家: %d/%d", Bukkit.getOnlinePlayers().size, Bukkit.getMaxPlayers()))
            .append("\n")

        val Max = Runtime.getRuntime().maxMemory()
        val Use = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        sb.append("内存: ").append(String.format("%.2f%%", Use / Max.toDouble() * 100)).append(" (")
            .append(NumProcess.unitByte(Max)).append("-").append(NumProcess.unitByte(Use)).append("=")
            .append(NumProcess.unitByte(Max - Use))
            .append(" 分配:").append(NumProcess.unitByte(Runtime.getRuntime().totalMemory())).append(")").append("\n")

        val df = DecimalFormat("#.00") // 保留小数点后两位

        val tps: MutableList<String> = ArrayList(4) // tps值有4个

        for (single in Bukkit.getTPS()) {
            tps.add(df.format(single))
        }
        val mspt = df.format(Bukkit.getTickTimes()[0] / 1000000).toDouble()
        sb.append("TPS: ").append(tps).append(" MSPT: ").append(mspt).append("\n")

        val dt = Bukkit.getServer().worldContainer.totalSpace
        val du = Bukkit.getServer().worldContainer.usableSpace
        val duse = dt - du
        sb.append("线程数: ").append(Thread.currentThread().threadGroup.activeCount()).append(" 磁盘: ")
            .append(NumProcess.unitByte(dt)).append("-").append(NumProcess.unitByte(duse)).append("=")
            .append(NumProcess.unitByte(du))

        msg.send(sb.toString())
    }




}