@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.StageController
import cn.izis.base.stage_home
import cn.izis.util.*
import cn.izis.util.rx.RxEvent
import com.jfoenix.controls.JFXTabPane
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javafx.fxml.FXML
import javafx.scene.input.KeyCode
import javafx.scene.layout.Pane
import org.controlsfx.control.StatusBar
import java.net.URL
import java.util.*

class HomeController:BaseController() {
    @FXML lateinit var tabPane:JFXTabPane
    @FXML lateinit var status_bar:StatusBar

    private val TAB_INFO = 0
    private val TAB_USER = 1
    private val TAB_ARRANGE = 2
    private val TAB_HINSTORY = 3
    private val TAB_HELP = 4

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        tabPane.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            when (newValue.toInt()){
                TAB_INFO -> send(RxEvent.refreshMatchInfo)
                TAB_USER -> send(RxEvent.refreshMatchUsers)
                TAB_ARRANGE -> send(RxEvent.refreshMatchArrange)
                TAB_HINSTORY -> send(RxEvent.refreshMatchHistory)
            }
        }

        status_bar.textProperty().bind(matchCurr.matchNameProperty)

//        subscribeEvent()
    }

//    override fun onReceive(rxEvent: RxEvent) {
//        super.onReceive(rxEvent)
//        when (rxEvent.code){
//            RxEvent.update_statusbar_text -> {
//                val text = rxEvent.`object` as String
//                status_bar.text = text
//            }
//        }
//    }

    override fun loadCompetition() {
        tabPane.tabs[TAB_INFO].content = stageController?.loadFxml("/fxml/tab_match_info.fxml")
        tabPane.tabs[TAB_USER].content = stageController?.loadFxml("/fxml/tab_match_users.fxml")
        tabPane.tabs[TAB_ARRANGE].content = stageController?.loadFxml("/fxml/tab_match_arrange.fxml")
        tabPane.tabs[TAB_HINSTORY].content = stageController?.loadFxml("/fxml/tab_match_history.fxml")
        tabPane.tabs[TAB_HELP].content = stageController?.loadFxml("/fxml/tab_help_center.fxml")

        getWindow()?.scene?.quickKey(KeyCode.DELETE){
            when(tabPane.selectionModel.selectedIndex){
                TAB_USER -> send(RxEvent.del_user)
                TAB_HINSTORY -> send(RxEvent.del_match)
            }
        }
    }

}