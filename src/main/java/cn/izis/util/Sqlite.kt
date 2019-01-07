package cn.izis.util

import cn.izis.bean.Match
import java.sql.DriverManager

fun getConn() = DriverManager.getConnection("jdbc:sqlite:match.db")

fun createMatch(match:Match): Int {
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0){
            val sql = "create table match(" +
                    " match_id integer primary key AUTOINCREMENT," +
                    " match_name text," +
                    " sponsor text," +
                    " organizer text," +
                    " co_organizer text," +
                    " supporting text," +
                    " match_arbitration text," +
                    " match_address text," +
                    " match_time_start text," +
                    " match_time_end text," +
                    " match_referee text," +
                    " match_arrange text )"
            statement.executeUpdate(sql)
        }

        val insertMatch = "insert into match values (" +
                null + "," +
                "\"${match.match_name}\"," +
                "\"${match.sponsor}\"," +
                "\"${match.organizer}\"," +
                "\"${match.co_organizer}\"," +
                "\"${match.supporting}\"," +
                "\"${match.match_arbitration}\"," +
                "\"${match.match_address}\"," +
                "${match.match_time_start}," +
                "${match.match_time_end}," +
                "\"${match.match_referee}\"," +
                "\"${match.match_arrange}\"" +
                ")"
        val result = statement.executeUpdate(insertMatch)

        it.close()

        return result
    }
}