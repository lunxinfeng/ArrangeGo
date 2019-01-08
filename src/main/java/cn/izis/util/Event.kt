package cn.izis.util

import cn.izis.util.rx.RxBus
import cn.izis.util.rx.RxEvent

fun send(code:Int,msg:Any = 0) = RxBus.getDefault().send(RxEvent(code,msg))