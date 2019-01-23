package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.util.mdToHtml
import javafx.scene.web.WebView
import java.net.URL
import java.util.*

class HelpCenterController:BaseController(){
    lateinit var webView:WebView
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val content = mdToHtml(javaClass.getResourceAsStream("/res/README.md"))
        println(content)
        webView.engine.loadContent(content)
    }
}