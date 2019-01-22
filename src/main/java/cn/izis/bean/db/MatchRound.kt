@file:Suppress("PropertyName")

package cn.izis.bean.db

import cn.izis.util.getTime
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import java.time.LocalDateTime

class MatchRound(
    val roundIndex: Int,
    val matchId:Int
) {
    var id = 0
    var status:Int //0 进行中  1已结束
        get() = statusProperty.get()
        set(value) {
            statusProperty.set(value)
        }
    val statusProperty = SimpleIntegerProperty(0)

    var time_start:Long //开始时间
        get() = matchTimeStartProperty.get()
        set(value) {
            matchTimeStartProperty.set(value)
        }
    val matchTimeStartProperty = SimpleLongProperty(LocalDateTime.now().getTime())


    override fun toString(): String {
        return "第${roundIndex}轮"
    }
}