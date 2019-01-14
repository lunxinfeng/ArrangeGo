package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.util.mdToHtml
import javafx.scene.web.WebView
import java.io.File
import java.net.URL
import java.util.*

class HelpCenterController:BaseController(){
    lateinit var webView:WebView
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val content = mdToHtml(File(System.getProperty("user.dir") + File.separator + "README.md"))
        println(content)
        webView.engine.loadContent(content)
    }
}