package org.encinet.kookmc

import org.encinet.kookmc.util.BindData
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

class Whitelist(var file: String) {
    var con: Connection? = null

    init {
        load()
    }

    @Throws(SQLException::class)
    fun load() {
        try {
            Class.forName("org.sqlite.JDBC")
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("Failed to load SQLite JDBC class", e)
        }
        con = DriverManager.getConnection("jdbc:sqlite:$file")
        con!!.createStatement().use { st ->
            // 无绑KOOK时 KOOK为0为管理 1为普通用户
            st.executeUpdate(
                "create table if not exists bind("
                        + "UUID VARCHAR(40) PRIMARY KEY NOT NULL,"
                        + "Name VARCHAR(20) NOT NULL UNIQUE,"
                        + "KOOK INTEGER NOT NULL UNIQUE DEFAULT 1);"
            )
        }
    }

    /**
     * 添加绑定
     * @param uuid 玩家uuid
     * @param name 玩家id
     * @param kook 玩家kook id
     * @return 成功即为true
     */
    fun add(uuid: UUID, name: String?, kook: Long): Boolean {
        val sql = "INSERT INTO bind (UUID, Name, KOOK) VALUES ('?', '?', '?');"
        try {
            con!!.prepareStatement(sql).use { pst ->
                pst.setString(1, uuid.toString())
                pst.setString(2, name)
                pst.setLong(3, kook)
                pst.executeUpdate()
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }
    // -------
    // 获取绑定
    // -------
    /**
     * 通过玩家uuid查询玩家绑定信息
     * @param uuid 玩家uuid
     * @return 查询到的信息 null是查不到或出现错误
     */
    fun getBind(uuid: UUID): BindData? {
        val sql = "SELECT * FROM bind WHERE UUID=?;"
        try {
            con!!.prepareStatement(sql).use { pst ->
                pst.setString(1, uuid.toString())
                pst.executeQuery().use { rs ->
                    if (rs.next()) {
                        val rsUUID = UUID.fromString(rs.getString("UUID"))
                        val rsName = rs.getString("Name")
                        val rsKOOK = rs.getLong("KOOK")
                        return BindData(rsUUID, rsName, rsKOOK)
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 通过玩家id查询玩家绑定信息
     * @param name 玩家id
     * @return 查询到的信息 null是查不到或出现错误
     */
    fun getBind(name: String?): BindData? {
        val sql = "SELECT * FROM bind WHERE Name=?;"
        try {
            con!!.prepareStatement(sql).use { pst ->
                pst.setString(1, name)
                pst.executeQuery().use { rs ->
                    if (rs.next()) {
                        val rsUUID = UUID.fromString(rs.getString("UUID"))
                        val rsName = rs.getString("Name")
                        val rsKOOK = rs.getLong("KOOK")
                        return BindData(rsUUID, rsName, rsKOOK)
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 通过玩家id查询玩家绑定信息
     * 支持模糊查询
     * @param name 玩家id 大小写任意 支持通配符模糊查询
     * @return 所有查询到的信息 null是出现错误
     */
    fun getBindFuzzy(name: String?): List<BindData>? {
        val list: MutableList<BindData> = ArrayList<BindData>()
        val sql = "SELECT * FROM bind WHERE LOWER(Name)=LOWER(?);"
        try {
            con!!.prepareStatement(sql).use { pst ->
                pst.setString(1, name)
                pst.executeQuery().use { rs ->
                    while (rs.next()) {
                        val rsUUID = UUID.fromString(rs.getString("UUID"))
                        val rsName = rs.getString("Name")
                        val rsKOOK = rs.getLong("KOOK")
                        list.add(BindData(rsUUID, rsName, rsKOOK))
                    }
                    return list
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 通过玩家KOOK查询玩家绑定信息
     * @param kook 玩家kook id
     * @return 查询到的信息 null是查不到或出现错误
     */
    fun getBind(kook: Long): BindData? {
        val sql = "SELECT * FROM bind WHERE KOOK=?;"
        try {
            con!!.prepareStatement(sql).use { pst ->
                pst.setLong(1, kook)
                pst.executeQuery().use { rs ->
                    if (rs.next()) {
                        val rsUUID = UUID.fromString(rs.getString("UUID"))
                        val rsName = rs.getString("Name")
                        val rsKOOK = rs.getLong("KOOK")
                        return BindData(rsUUID, rsName, rsKOOK)
                    }
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }

    // -------
    // 是否存在
    // -------
    operator fun contains(uuid: UUID): Boolean {
        return getBind(uuid) != null
    }

    operator fun contains(name: String?): Boolean {
        return getBind(name) != null
    }

    operator fun contains(kook: Long): Boolean {
        return getBind(kook) != null
    }

    /**
     * 删除绑定
     * @param kook kook id
     * @return 成功即为true
     */
    fun remove(kook: Long): Boolean {
        val sql = "DELETE FROM bind WHERE KOOK=?;"
        try {
            con!!.prepareStatement(sql).use { pst ->
                pst.setLong(1, kook)
                pst.executeUpdate()
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }
}
