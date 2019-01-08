package cn.izis.util

import cn.izis.bean.Match
import java.sql.DriverManager

fun getConn() = DriverManager.getConnection("jdbc:sqlite:match.db")

fun createMatch(match: Match): Int {
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0) {
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
    return 0
}

fun queryMatch(matchId: Int = 0): Match? {
    getConn()?.let {
        val statement = it.createStatement()
        val queryLastSql = if (matchId == 0)
            "select * from match order by match_id desc limit 1"
        else
            "select * from match where match_id = $matchId limit 1"

        val result = statement.executeQuery(queryLastSql)
        val match = Match(
            match_name = result.getString("match_name"),
            sponsor = result.getString("sponsor"),
            organizer = result.getString("organizer"),
            co_organizer = result.getString("co_organizer"),
            supporting = result.getString("supporting"),
            match_arbitration = result.getString("match_arbitration"),
            match_address = result.getString("match_address"),
            match_time_start = result.getLong("match_time_start"),
            match_time_end = result.getLong("match_time_end"),
            match_referee = result.getString("match_referee"),
            match_arrange = result.getString("match_arrange"),
            match_id = result.getInt("match_id")
        )
        it.close()
        return match
    }
    return null
}

fun updateMatch(match: Match): Int {
    getConn()?.let {
        val statement = it.createStatement()
        val updateSql = "update match set " +
                "match_name = '${match.match_name}'," +
                "sponsor = '${match.sponsor}'," +
                "organizer = '${match.organizer}'," +
                "co_organizer = '${match.co_organizer}'," +
                "supporting = '${match.supporting}'," +
                "match_arbitration = '${match.match_arbitration}'," +
                "match_address = '${match.match_address}'," +
                "match_time_start = ${match.match_time_start}," +
                "match_time_end = ${match.match_time_end}," +
                "match_referee = '${match.match_referee}'," +
                "match_arrange = '${match.match_arrange}' " +
                "where match_id = ${match.match_id}"

        val result = statement.executeUpdate(updateSql)
        it.close()
        return result
    }
    return 0
}