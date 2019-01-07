package cn.izis.util

import java.util.logging.Logger

var debug = true
val logger = Logger.getGlobal()

fun info(msg: String) {
    if (debug)
        logger.info(msg)
}