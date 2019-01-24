@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.bean.db.MatchUser
import cn.izis.util.info
import cn.izis.util.printNode
import cn.izis.util.rx.RxEvent
import javafx.event.ActionEvent
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class MatchPrinterController:BaseController() {
    lateinit var vboxPrinter:VBox
    lateinit var label_title:Label
    lateinit var label_referee:Label
    lateinit var label_arrange:Label
    lateinit var stackPaneContent:StackPane
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        label_title.text = matchCurr.match_name
        label_referee.text = "裁判长：${matchCurr.match_referee}"
        label_arrange.text = "编排长：${matchCurr.match_arrange}"
        subscribeEvent()
    }

    override fun loadCompetition() {
        getWindow()?.isResizable = false
    }

    override fun onReceive(rxEvent: RxEvent) {
        super.onReceive(rxEvent)
        when(rxEvent.code){
            RxEvent.print_users -> {
                val tableView = rxEvent.`object` as TableView<MatchUser>
                info("receive：$tableView")
                stackPaneContent.children.add(tableView)
            }
        }
    }

    fun onCancel(actionEvent: ActionEvent) {
        closeSelf()
    }

    fun onPrint(actionEvent: ActionEvent) {
        printNode(vboxPrinter)
    }
}