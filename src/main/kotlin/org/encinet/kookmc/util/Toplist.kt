package org.encinet.kookmc.util

import org.bukkit.Bukkit
import org.bukkit.Statistic
import java.util.*

abstract class Toplist {
    // 每个page多少行
    private var line = 10

    /**
     * @param statistic 统计
     * @param tl        TopList实例
     * @param topText   标题名
     * @param page      页码树
     * @return 排行榜文本
     */
    operator fun get(
        statistic: Statistic?,
        tl: Toplist,
        topText: String?,
        page: Int
    ): String {
        val start = line * (page - 1) + 1
        val end = line * page
        val oPlayers = Bukkit.getOfflinePlayers()
        val online = TreeMap<Int, String?>(Comparator.reverseOrder()) // 倒序Treemap
        for (oPlayer in oPlayers) {
            online[oPlayer.getStatistic(statistic!!)] = oPlayer.name
        }
        val sb = StringBuilder()
        val size = online.size
        val maxPage = pages(size)
        sb.append(topText).append(" ").append(page).append("/").append(maxPage).append("\n")
        if (page > maxPage) return sb.append("无").toString()
        var num = 1
        for ((key, value) in online) {
            if (num in start..end) {
                sb.append(num).append(".").append(value).append(" - ").append(tl.unit(key)).append("\n")
            } else if (num > end) break
            num++
        }
        return sb.toString()
    }

    /**
     * @param num 值
     * @return 处理后的值
     */
    abstract fun unit(num: Int): String?

    /**
     * @param length 总长度
     * @return 总页码
     */
    private fun pages(length: Int): Int {
        val division = length / line
        return if (length % line > 0) division + 1 else division
    }
}