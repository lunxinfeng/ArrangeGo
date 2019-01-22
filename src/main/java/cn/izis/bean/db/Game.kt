package cn.izis.bean.db

class Game {
    var id = 0
    var num = 0 //台次
    var matchId = 0 //赛事id
    var roundIndex = 0  //轮次索引
    var gameName = ""   //对局名称
    var blackId = 0 //黑方id
    var whiteId = 0 //白方id
    var blackName = ""  //黑方名字
    var whiteName = ""  //白方名字
    var status = "" //对局状态
    var blackScore = 0  //黑方得分
    var whiteScore = 0  //白方得分
    var blackTotalScore = 0 //黑方积分
    var whiteTotalScore = 0 //白方积分
}