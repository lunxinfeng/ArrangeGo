package cn.izis.bean

import javafx.beans.property.SimpleBooleanProperty

class GameInfo {
    var num = 0//台次
    var blackId = 0//黑方
    var whiteId = 0//白方
    var blackName = ""//黑方名字
    var whiteName = ""//白方名字
    var blackScore = 0//黑方得分
    var whiteScore = 0//白方得分
    var blackTotalScore = 0//黑方积分
    var whiteTotalScore = 0//白方积分
    var status = ""//比赛状态

    override fun equals(other: Any?): Boolean {
        if (other is GameInfo)
            return num == other.num
        return super.equals(other)
    }
}