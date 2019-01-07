package cn.izis.util

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun getTime(date:LocalDate,time:LocalTime) = Timestamp.valueOf(LocalDateTime.of(date,time)).time