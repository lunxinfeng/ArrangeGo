package cn.izis.util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.*
import java.util.*

fun getTime(date:LocalDate,time:LocalTime) = Timestamp.valueOf(LocalDateTime.of(date,time)).time

fun LocalDateTime.getTime() = getTime(this.toLocalDate(),this.toLocalTime())

fun Long.toTime()= SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(this))

fun Long.toLocalDateTime() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())