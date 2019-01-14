package cn.izis.util

import cn.izis.bean.Match
import cn.izis.bean.MatchRound
import cn.izis.bean.User
import java.lang.Exception
import java.sql.DriverManager

fun getConn() = DriverManager.getConnection("jdbc:sqlite:match.db")

fun createMatch(match: Match): Int {
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0) {
            val createMatchSql = "create table match(" +
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
            statement.executeUpdate(createMatchSql)

            val createMatchRoundSql = "create table match_round(" +
                    " id integer primary key AUTOINCREMENT," +
                    " match_id integer," +
                    " round_index integer," +
                    " time_start text)"
            statement.executeUpdate(createMatchRoundSql)
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
        try {
            val statement = it.createStatement()
            val queryLastSql = if (matchId == 0)
//            "select * from match order by match_id desc limit 1"
                "SELECT m.*,r.round_index,r.time_start FROM (SELECT * FROM match ORDER BY match_id DESC limit 1) AS m LEFT JOIN match_round AS r"
            else
//            "select * from match where match_id = $matchId"
                "select m.*,r.round_index,r.time_start from match as m LEFT JOIN match_round as r where m.match_id = $matchId"

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
            ).apply {
                while (result.next()){
                    val roundIndex = result.getInt("round_index")
                    if (roundIndex <= 0) continue
                    val timeStart = result.getLong("time_start")
                    match_round_list.add(MatchRound(roundIndex,this.match_id).apply {
                        this.time_start = timeStart
                    })
                }
            }
            return match
        } catch (e: Exception) {
            e.printStackTrace()
            info(e.message?:"未知错误")
        } finally {
            it.close()
        }
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

fun saveMatchRound(match: Match): Int {
    var result = 0
    getConn()?.let {
        val delSql = "delete from match_round where match_id = ${match.match_id}"
        val statement = it.createStatement()
        result = statement.executeUpdate(delSql)

        if (match.match_round_list.size > 0){
            val insertSql = "insert into match_round values ( null , ${match.match_id} , ?,?)"
            val preStatement = it.prepareStatement(insertSql)
            match.match_round_list.forEach { round ->
                preStatement.setInt(1,round.roundIndex)
                preStatement.setLong(2,round.time_start)
                preStatement.addBatch()
            }
            result = preStatement.executeBatch().size
        }
        it.close()
    }
    return result
}

fun saveUsers(matchId:Int, users:List<User>): Int {
    var result = 0
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match_users'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0) {
            val createUsersSql = "create table match_users(" +
                    " id integer primary key AUTOINCREMENT," +
                    " match_id integer," +
                    " name text," +
                    " sex text," +
                    " company text," +
                    " phone text," +
                    " age text)"
            statement.executeUpdate(createUsersSql)
        }

        val delSql = "delete from match_users where match_id = $matchId"
        result = statement.executeUpdate(delSql)

        if (users.isNotEmpty()){
            val insertSql = "insert into match_users values ( null , $matchId , ?,?,?,?,?)"
            val preStatement = it.prepareStatement(insertSql)
            users.forEach { user ->
                preStatement.setString(1,user.name)
                preStatement.setString(2,user.sex)
                preStatement.setString(3,user.company)
                preStatement.setString(4,user.phone)
                preStatement.setString(5,user.age)
                preStatement.addBatch()
            }
            result = preStatement.executeBatch().size
        }
        it.close()
    }
    return result
}

fun queryUsers(matchId:Int): MutableList<User> {
    val list = mutableListOf<User>()
    getConn()?.let {
        val statement = it.createStatement()
        val queryUsersSql = "select * from match_users where match_id = $matchId"
        val result = statement.executeQuery(queryUsersSql)

        while (result.next()){
            list.add(User().apply {
                name = result.getString("name")
                sex = result.getString("sex")
                company = result.getString("company")
                phone = result.getString("phone")
                age = result.getString("age")
            })
        }

        it.close()
    }
    return list
}

fun queryAllMatchsWithOutRound(): MutableList<Match> {
    val list = mutableListOf<Match>()
    getConn()?.let {
        val statement = it.createStatement()
        val queryUsersSql = "select * from match"
        val result = statement.executeQuery(queryUsersSql)

        while (result.next()){
            list.add(Match(
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
            ))
        }

        it.close()
    }
    return list
}