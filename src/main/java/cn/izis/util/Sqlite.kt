package cn.izis.util

import cn.izis.bean.GameInfo
import cn.izis.bean.db.*
import java.lang.Exception
import java.sql.DriverManager
import java.sql.Types

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
                    " match_arrange text" +
                    " )"
            statement.executeUpdate(createMatchSql)

            val createMatchRoundSql = "create table match_round(" +
                    " id integer primary key AUTOINCREMENT," +
                    " match_id integer," +
                    " round_index integer," +
                    " status integer," +
                    " time_start text," +
                    " CONSTRAINT a FOREIGN KEY (match_id) REFERENCES match (match_id) ON DELETE CASCADE)"
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

        //创建比赛默认添加一轮
        val queryMatchId = "select match_id from match order by match_id desc limit 1"
        val id = statement.executeQuery(queryMatchId).getInt("match_id")
        val round = MatchRound(1,id)
        val insertRound = "replace into match_round values ( null , ${round.matchId} , ${round.roundIndex},${round.status},${round.time_start})"
        statement.executeUpdate(insertRound)

        it.close()

        return result
    }
    return 0
}

fun queryMatch(matchId: Int = 0): Match? {
    getConn()?.let {
        try {
            val statement = it.createStatement()
            val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match'"
            val resultSet = statement.executeQuery(tableIsExist)
            if (resultSet.getInt("count(*)") == 0) {
                return null
            }

            val queryLastSql = if (matchId == 0)
//            "select * from match order by match_id desc limit 1"
                "SELECT m.*,r.* FROM (SELECT * FROM match ORDER BY match_id DESC limit 1) AS m LEFT JOIN match_round AS r WHERE r.match_id = m.match_id"
            else
//            "select * from match where match_id = $matchId"
                "select m.*,r.* from match as m LEFT JOIN match_round as r where m.match_id = $matchId AND r.match_id = $matchId"

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
                while (result.next()) {
                    val roundIndex = result.getInt("round_index")
                    if (roundIndex <= 0) continue
                    val timeStart = result.getLong("time_start")
                    match_round_list.add(MatchRound(roundIndex, this.match_id).apply {
                        this.id = result.getInt("id")
                        this.status = result.getInt("status")
                        this.time_start = timeStart
                    })
                }
            }
            return match
        } catch (e: Exception) {
            e.printStackTrace()
            info(e.message ?: "未知错误")
        } finally {
            it.close()
        }
    }
    return null
}

fun updateMatch(match: Match): Int {
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0) {
            return 0
        }
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
//        val statement = it.createStatement()
//        val delSql = "delete from match_round where match_id = ${match.match_id}"
//        result = statement.executeUpdate(delSql)

        if (match.match_round_list.size > 0) {
            val insertSql = "replace into match_round values ( ? , ${match.match_id} , ?,?,?)"
            val preStatement = it.prepareStatement(insertSql)
            match.match_round_list.forEach { round ->
                if (round.id == 0)
                    preStatement.setNull(1, Types.INTEGER)
                else
                    preStatement.setInt(1, round.id)
                preStatement.setInt(2, round.roundIndex)
                preStatement.setInt(3, round.status)
                preStatement.setLong(4, round.time_start)
                preStatement.addBatch()
            }
            result = preStatement.executeBatch().size
        }
        it.close()
    }
    return result
}

fun saveUsers(matchId: Int, users: List<MatchUser>): Int {
    var result = 0
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match_users'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0) {
            val createUsersSql = "create table match_users(" +
                    " id integer primary key AUTOINCREMENT," +
                    " match_id integer," +
                    " group_id integer," +
                    " num integer," +
                    " name text," +
                    " sex text," +
                    " company text," +
                    " phone text," +
                    " age text," +
                    " CONSTRAINT a FOREIGN KEY (match_id) REFERENCES match (match_id) ON DELETE CASCADE)"
            statement.executeUpdate(createUsersSql)
        }

        if (users.isNotEmpty()) {
            val insertSql = "replace into match_users values (?, $matchId , ?,?,?,?,?,?,?)"
            val preStatement = it.prepareStatement(insertSql)
            users.forEach { user ->
                if (user.id == 0)
                    preStatement.setNull(1, Types.INTEGER)
                else
                    preStatement.setInt(1, user.id)
                preStatement.setInt(2, user.groupId)
                preStatement.setInt(3, user.index.toInt())
                preStatement.setString(4, user.name)
                preStatement.setString(5, user.sex)
                preStatement.setString(6, user.company)
                preStatement.setString(7, user.phone)
                preStatement.setString(8, user.age)
                preStatement.addBatch()
            }
            result = preStatement.executeBatch().size
        }
        it.close()
    }
    return result
}

fun queryUsers(matchId: Int): MutableList<MatchUser> {
    val list = mutableListOf<MatchUser>()
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match_users'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") != 0) {
            val queryUsersSql = "select * from match_users where match_id = $matchId order by num"
            val result = statement.executeQuery(queryUsersSql)

            while (result.next()) {
                list.add(MatchUser().apply {
                    name = result.getString("name")
                    sex = result.getString("sex")
                    company = result.getString("company")
                    phone = result.getString("phone")
                    age = result.getString("age")
                    this.matchId = result.getInt("match_id")
                    groupId = result.getInt("group_id")
                    index = result.getInt("num").toString()
                    id = result.getInt("id")
                })
            }
        }
        it.close()
    }
    return list
}

fun queryAllMatchsWithOutRound(): MutableList<Match> {
    val list = mutableListOf<Match>()
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0) {
            return list
        }
        val queryUsersSql = "select * from match"
        val result = statement.executeQuery(queryUsersSql)

        while (result.next()) {
            list.add(
                Match(
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
            )
        }

        it.close()
    }
    return list
}

fun queryRoundsByMatchId(matchId: Int): MutableList<MatchRound> {
    val list = mutableListOf<MatchRound>()
    getConn()?.let {
        val statement = it.createStatement()
        val queryUsersSql = "select * from match_round where match_id = $matchId"
        val result = statement.executeQuery(queryUsersSql)

        while (result.next()) {
            list.add(
                MatchRound(
                    roundIndex = result.getInt("round_index"),
                    matchId = matchId
                ).apply {
                    time_start = result.getLong("time_start")
                    status = result.getInt("status")
                }
            )
        }

        it.close()
    }
    return list
}


fun saveArranges(matchId: Int, arranges: List<Arrange>): Int {
    var result = 0
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match_arranges'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0) {
            val createUsersSql = "create table match_arranges(" +
                    " id integer primary key AUTOINCREMENT," +
                    " match_id integer," +
                    " group_id integer," +
                    " round_index integer," +
                    " user_id integer," +
                    " other_id integer," +
                    " result text," +
                    " score integer," +
                    " total_score integer," +
                    " win_type text," +
                    " add_user text," +
                    " add_time text," +
                    " CONSTRAINT a FOREIGN KEY (match_id) REFERENCES match (match_id) ON DELETE CASCADE)"
            statement.executeUpdate(createUsersSql)
        }

        if (arranges.isNotEmpty()) {
            val insertSql = "replace into match_arranges values (?, $matchId , ?,?,?,?,?,?,?,?,?,?)"
            val preStatement = it.prepareStatement(insertSql)
            arranges.forEach { arrange ->
                if (arrange.id == 0)
                    preStatement.setNull(1, Types.INTEGER)
                else
                    preStatement.setInt(1, arrange.id)
                preStatement.setInt(2, arrange.groupId)
                preStatement.setInt(3, arrange.roundIndex)
                preStatement.setInt(4, arrange.userId)
                preStatement.setInt(5, arrange.otherId)
                preStatement.setString(6, arrange.result)
                preStatement.setInt(7, arrange.score)
                preStatement.setInt(8, arrange.total_score)
                preStatement.setString(9, arrange.win_type)
                preStatement.setInt(10, arrange.add_user)
                preStatement.setString(11, arrange.add_time)
                preStatement.addBatch()
            }
            result = preStatement.executeBatch().size
        }
        it.close()
    }
    return result
}

fun saveGames(matchId: Int, games: List<Game>): Int {
    var result = 0
    getConn()?.let {
        val statement = it.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match_games'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") == 0) {
            val createUsersSql = "create table match_games(" +
                    " id integer primary key AUTOINCREMENT," +
                    " match_id integer," +
                    " round_index integer," +
                    " num integer," +
                    " black_id integer," +
                    " white_id integer," +
                    " black_name text," +
                    " white_name text," +
                    " gameName text," +
                    " status text," +
                    " CONSTRAINT a FOREIGN KEY (match_id) REFERENCES match (match_id) ON DELETE CASCADE)"
            statement.executeUpdate(createUsersSql)
        }

        if (games.isNotEmpty()) {
            val insertSql = "replace into match_games values (?, $matchId , ?,?,?,?,?,?,?,?)"
            val preStatement = it.prepareStatement(insertSql)
            games.forEach { game ->
                if (game.id == 0)
                    preStatement.setNull(1, Types.INTEGER)
                else
                    preStatement.setInt(1, game.id)
                preStatement.setInt(2, game.roundIndex)
                preStatement.setInt(3, game.num)
                preStatement.setInt(4, game.blackId)
                preStatement.setInt(5, game.whiteId)
                preStatement.setString(6, game.blackName)
                preStatement.setString(7, game.whiteName)
                preStatement.setString(8, game.gameName)
                preStatement.setString(9, game.status)
                preStatement.addBatch()
            }
            result = preStatement.executeBatch().size
        }
        it.close()
    }
    return result
}

fun queryGameInfo(matchId: Int,roundIndex:Int): MutableList<GameInfo> {
    val list = mutableListOf<GameInfo>()
    getConn()?.let { conn ->
        val statement = conn.createStatement()
        val tableIsExist = "select count(*)  from sqlite_master where type='table' and name = 'match_games'"
        val resultSet = statement.executeQuery(tableIsExist)
        if (resultSet.getInt("count(*)") != 0) {
            val sql = "select a.user_id,a.score,a.total_score,g.* from match_games as g JOIN match_arranges as a on g.black_id = a.user_id or g.white_id = a.user_id where g.match_id = $matchId AND g.round_index = $roundIndex AND a.match_id = $matchId AND a.round_index = $roundIndex"
            val result = statement.executeQuery(sql)


            while (result.next()) {
                val num = result.getInt("num")
                val score = result.getInt("score")
                val totalScore = result.getInt("total_score")
                val userId = result.getInt("user_id")
                val blackId = result.getInt("black_id")
                val whiteId = result.getInt("white_id")
                val blackName = result.getString("black_name")
                val whiteName = result.getString("white_name")
                val status = result.getString("status")

                val gameInfo = GameInfo().apply {
                    this.num = num
                    this.blackId = blackId
                    this.whiteId = whiteId
                    this.blackName = blackName
                    this.whiteName = whiteName
                    this.status = status
                    if (userId == blackId){
                        this.blackScore = score
                        this.blackTotalScore = totalScore
                    }else{
                        this.whiteScore = score
                        this.whiteTotalScore = totalScore
                    }
                }

                if (list.contains(gameInfo)){
                    list.filter { it.num == num }
                        .map { it.apply {
                            if (userId == blackId){
                                this.blackScore = score
                                this.blackTotalScore = totalScore
                            }else{
                                this.whiteScore = score
                                this.whiteTotalScore = totalScore
                            }
                        } }
                }else{
                    list.add(gameInfo)
                }
            }
        }

        conn.close()
    }
    list.sortBy {
        it.num
    }
    return list
}

fun updateMatchArrange(
    matchId: Int,
    roundIndex: Int,
    gameInfo: GameInfo
) : Int{
    var result = 0
    getConn()?.let{
        val sql = "UPDATE match_arranges set score = ?,total_score = ? WHERE match_id = ? AND round_index = ? AND user_id = ?"
        val preStatement = it.prepareStatement(sql)
        preStatement.setInt(1,gameInfo.blackScore)
        preStatement.setInt(2,gameInfo.blackTotalScore)
        preStatement.setInt(3,matchId)
        preStatement.setInt(4,roundIndex)
        preStatement.setInt(5,gameInfo.blackId)
        preStatement.addBatch()

        preStatement.setInt(1,gameInfo.whiteScore)
        preStatement.setInt(2,gameInfo.whiteTotalScore)
        preStatement.setInt(3,matchId)
        preStatement.setInt(4,roundIndex)
        preStatement.setInt(5,gameInfo.whiteId)
        preStatement.addBatch()

        result = preStatement.executeBatch().size
        it.close()
    }
    return result
}

fun updateMatchGame(
    matchId: Int,
    roundIndex: Int,
    gameInfo: GameInfo
): Int {
    var result = 0
    getConn()?.let{
        val statement = it.createStatement()
        val sql = "UPDATE match_games set status = '${gameInfo.status}' WHERE match_id = $matchId AND round_index = $roundIndex AND num = ${gameInfo.num}"
        result = statement.executeUpdate(sql)
        it.close()
    }
    return result
}

fun updateMatchRound(
    matchId: Int,
    roundIndex: Int,
    round: MatchRound
): Int {
    var result = 0
    getConn()?.let{
        val statement = it.createStatement()
        val sql = "UPDATE match_round set status = '${round.status}' WHERE match_id = $matchId AND round_index = $roundIndex"
        result = statement.executeUpdate(sql)
        it.close()
    }
    return result
}

fun delMatch(matchId:Int): Int {
    var result = 0
    getConn()?.let{
        val statement = it.createStatement()
        val foreignKey = "PRAGMA foreign_keys=ON"
        statement.executeUpdate(foreignKey)
        val sql = "delete from match WHERE match_id = $matchId"
        result = statement.executeUpdate(sql)
        it.close()
    }
    return result
}

fun delUser(matchId: Int,userId:Int): Int {
    var result = 0
    getConn()?.let{
        val statement = it.createStatement()
        val sql = "delete from match_users WHERE match_id = $matchId AND id = $userId"
        result = statement.executeUpdate(sql)
        it.close()
    }
    return result
}

