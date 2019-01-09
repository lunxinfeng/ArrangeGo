@file:Suppress("PropertyName")

package cn.izis.bean

import cn.izis.util.getTime
import javafx.beans.property.SimpleLongProperty
import java.time.LocalDateTime

class MatchRound(
    val roundIndex: Int,
    val matchId:Int
) {
    var time_start:Long //开始时间
        get() = matchTimeStartProperty.get()
        set(value) {
            matchTimeStartProperty.set(value)
        }
    val matchTimeStartProperty = SimpleLongProperty(LocalDateTime.now().getTime())
}