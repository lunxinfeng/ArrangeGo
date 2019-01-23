package cn.izis.util

import cn.izis.arrange.Hungary
import cn.izis.arrange.UserApplyFrom
import cn.izis.arrange.UserApplyFromMessage
import cn.izis.arrange.UserSchedule
import cn.izis.bean.db.Arrange
import cn.izis.bean.db.Game
import cn.izis.bean.db.MatchUser
import cn.izis.work.matchCurr

fun arrange(matchId: Int, roundIndex: Int, matchUsers: MutableList<MatchUser>) {
    if (roundIndex == 1) {
        arrangeFirst(matchId, matchUsers)
    } else {
        arrangeNext(matchId, roundIndex, matchUsers)
    }
}

private fun arrangeNext(matchId: Int, roundIndex: Int, matchUsers: MutableList<MatchUser>) {
    val listArrange = mutableListOf<Arrange>()
    val listGame = mutableListOf<Game>()
    var number = 0
    val asc = roundIndex % 2 != 0

    val userSchedule = UserSchedule()

    val list_uaf = mutableListOf<UserApplyFrom>()
    val list_uafm = mutableListOf<UserApplyFromMessage>()

    val listUsers = queryUsers(matchId)
    if (listUsers.size % 2 !=0 ){
        listUsers.remove(MatchUser().apply { id = 9191 })
    }
    listUsers.forEach {
        list_uaf.add(UserApplyFrom(it.id))
    }
//    if (list_uaf.size % 2 != 0)
//        list_uaf.add(UserApplyFrom(9191))

    getConn()?.let { conn ->
        list_uaf.forEach {
            val uafm = UserApplyFromMessage()
            val list = mutableListOf<UserApplyFrom>()

            val statement = conn.createStatement()
            var sql = "SELECT * FROM match_users WHERE match_id = $matchId AND id = ${it.userid}"
            var resultSet = statement.executeQuery(sql)
            if (resultSet.next()) {
                uafm.num = resultSet.getInt("num")
                uafm.name = resultSet.getString("name")

                sql =
                        "SELECT total_score FROM match_arranges WHERE match_id = $matchId AND user_id = ${it.userid} ORDER BY total_score limit 1"
                resultSet = statement.executeQuery(sql)
                uafm.totalscore = if (resultSet.next()) resultSet.getInt("total_score") else 0

                sql =
                        "SELECT other_id FROM match_arranges WHERE match_id = $matchId AND user_id = ${it.userid} ORDER BY total_score limit 1"
                resultSet = statement.executeQuery(sql)
                while (resultSet.next()) {
                    list.add(UserApplyFrom(resultSet.getInt("other_id")))
                }

                uafm.list = list
                uafm.userid = it.userid
                list_uafm.add(uafm)
            }

        }
        conn.close()
    }
    userSchedule.uaF_list = list_uaf
    userSchedule.uafM_list = list_uafm

    userSchedule.MakeSchedule(asc)

//    info(userSchedule.toString())

    val hungary = Hungary()
    hungary.Test(userSchedule, 0, 0, 0)

    val list_es = hungary.getMatchSchdule(userSchedule.uafM_list)
    if (list_es.size == 0 || list_es.size != userSchedule.uafM_list.size / 2) {
        return
    }
    list_es.forEach {
        getConn()?.let { conn ->
            val statement = conn.createStatement()
            /*
             *   第二步：先后手的确定。在尽可能变换参赛者上一轮先后手的条件下，同时尽可能地充分平衡参赛者的先后手。
             *   先后手次数相同时，单轮次小号执黑先行，双轮次大号执黑先行。先后手不相同时，先手多的一方执白后走，先手少的一方执黑先行。
             *   需准备值：上一轮双方的执黑白情况，双方执黑各自总次数
             */
            //自己执黑次数
            val selfBlackCountSql =
                "SELECT COUNT(*) FROM match_games WHERE match_id = $matchId AND black_id = ${it.userid}"
            //对手执黑次数
            val otherBlackCountSql =
                "SELECT COUNT(*) FROM match_games WHERE match_id = $matchId AND black_id = ${it.ouserid}"

            //自己上轮执黑情况
            val selfBlackPreSql =
                "SELECT COUNT(*) FROM match_games WHERE match_id = $matchId AND black_id = ${it.userid} AND round_index = $roundIndex"
            //对手上轮执黑情况
            val otherBlackPreSql =
                "SELECT COUNT(*) FROM match_games WHERE match_id = $matchId AND black_id = ${it.ouserid} AND round_index = $roundIndex"

            var resultSet = statement.executeQuery(selfBlackPreSql)
            val selfBlackPre = resultSet.getInt("count(*)") > 0
            resultSet = statement.executeQuery(otherBlackPreSql)
            val otherBlackPre = resultSet.getInt("count(*)") > 0

            val selfBlackCurr: Boolean //自己当前轮次执黑情况
            val otherBlackCurr: Boolean//对手当前轮次执黑情况

            // 如果能够相容，则不需要后续判断，直接以此先手次序为准
            if ((selfBlackPre && !otherBlackPre) || (!selfBlackPre && otherBlackPre)) {
                selfBlackCurr = !selfBlackPre
                otherBlackCurr = !otherBlackPre
            } else {
                resultSet = statement.executeQuery(selfBlackCountSql)
                val selfBlackCount = resultSet.getInt("count(*)") // userid总执黑次数
                resultSet = statement.executeQuery(otherBlackCountSql)
                val otherBlackCount = resultSet.getInt("count(*)") // otherid总执黑次数

                if (selfBlackCount == otherBlackCount) {
                    if (roundIndex % 2 == 0 && it.user_num > it.ouser_num) {// 偶数轮，大号先行
                        selfBlackCurr = true
                        otherBlackCurr = false
                    } else {
                        selfBlackCurr = false
                        otherBlackCurr = true
                    }
                } else {
                    if (selfBlackCount > otherBlackCount) {
                        selfBlackCurr = true
                        otherBlackCurr = false
                    } else {
                        selfBlackCurr = false
                        otherBlackCurr = true
                    }
                }
            }


            //第三步：更新数据库，创建新一轮对局和编排表
            if (selfBlackCurr) {
                val first = matchUsers.filter { item ->
                    item.id == it.userid
                }[0]
                val second = matchUsers.filter { item ->
                    item.id == it.ouserid
                }[0]

                number = generate(first, second, matchId, roundIndex, listArrange, listGame, number,
                    matchUsers.any { it.id == 9191 }
                )
            }else{
                val first = matchUsers.filter { item ->
                    item.id == it.ouserid
                }[0]
                val second = matchUsers.filter { item ->
                    item.id == it.userid
                }[0]

                number = generate(first, second, matchId, roundIndex, listArrange, listGame, number,
                    matchUsers.any { it.id == 9191 }
                )
            }

            conn.close()
        }
    }

    saveArranges(matchCurr.match_id, listArrange)
    saveGames(matchCurr.match_id, listGame)
}


private fun arrangeFirst(matchId: Int, matchUsers: MutableList<MatchUser>) {
    val listArrange = mutableListOf<Arrange>()
    val listGame = mutableListOf<Game>()
    var number = 0
    var matchUserLast: MatchUser? = null
    if (matchUsers.size % 2 != 0) {
        matchUserLast = matchUsers.removeAt(matchUsers.size - 1)
    }

    for (i in 0 until matchUsers.size step 2) {
        val first = matchUsers[i]
        val second = matchUsers[i + 1]

        number = generate(first, second, matchId, 1, listArrange, listGame, number)
    }

    matchUserLast?.let { lastUser ->
        val emptyUser = MatchUser().apply {
            id = 9191
            this.matchId = matchId
            groupId = lastUser.groupId
            name = "轮空"
        }
        saveUsers(matchId, listOf(emptyUser))
        number = generate(lastUser, emptyUser, matchId, 1, listArrange, listGame, number, true)
    }

    saveArranges(matchCurr.match_id, listArrange)
    saveGames(matchCurr.match_id, listGame)
}

private fun generate(
    first: MatchUser,
    second: MatchUser,
    matchId: Int,
    roundIndex: Int,
    listArrange: MutableList<Arrange>,
    listGame: MutableList<Game>,
    number: Int,
    hasEmpty: Boolean = false
): Int {
    var number1 = number
    val black = Arrange().apply {
        this.matchId = matchId
        groupId = first.groupId
        this.roundIndex = roundIndex
        userId = first.id
        otherId = second.id
    }
    val white = Arrange().apply {
        this.matchId = matchId
        groupId = first.groupId
        this.roundIndex = roundIndex
        userId = second.id
        otherId = first.id
    }
    listArrange.addAll(arrayOf(black, white))

    listGame.add(Game().apply {
        num = ++number1
        this.matchId = matchId
        this.roundIndex = roundIndex
        blackId = black.userId
        whiteId = white.userId
        blackName = first.name
        whiteName = second.name
        gameName = "$blackName VS $whiteName"
        status = if (hasEmpty){
            if (blackId == 9191)
                "负"
            else if (whiteId == 9191)
                "胜"
            else
                "VS"
        } else
            "VS"
        if (hasEmpty) {
            blackScore = 2
            blackTotalScore = 2
        }
    })
    return number1
}