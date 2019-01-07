package cn.izis.bean

import java.sql.Timestamp


data class Match(
        val match_name:String,
        val sponsor:String, //举办单位
        val organizer:String, //承办单位
        val co_organizer:String, //协办单位
        val supporting:String, //赞助单位
        val match_arbitration:String, //比赛仲裁
        val match_address:String, //比赛地点
        val match_time_start:Long, //比赛时间开始
        val match_time_end:Long, //比赛时间结束
        val match_referee:String, //裁判长
        val match_arrange:String //编排长
)

data class Person(
        val index:Int,//编号
        val name:String,//名字
        val age:Int,//年龄
        val sex:Int,//性别  1男 2女
        val company:String,//公司
        val phone:String//电话
)