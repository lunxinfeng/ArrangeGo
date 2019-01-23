@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.StageController
import cn.izis.util.Transformer
import cn.izis.util.info
import cn.izis.util.rx.RxEvent
import cn.izis.util.send
import cn.izis.util.sub
import com.jfoenix.controls.JFXTabPane
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import org.controlsfx.control.StatusBar
import java.net.URL
import java.util.*

class HomeController:BaseController() {
    @FXML lateinit var tabPane:JFXTabPane
    @FXML lateinit var status_bar:StatusBar

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        tabPane.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            when (newValue.toInt()){
                0 -> send(RxEvent.refreshMatchInfo)
                1 -> send(RxEvent.refreshMatchUsers)
                2 -> send(RxEvent.refreshMatchArrange)
                3 -> send(RxEvent.refreshMatchHistory)
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

    override fun setStageController(stageController: StageController) {
        super.setStageController(stageController)
        tabPane.tabs[0].content = stageController.loadFxml("/fxml/tab_match_info.fxml")
        tabPane.tabs[1].content = stageController.loadFxml("/fxml/tab_match_users.fxml")
        tabPane.tabs[2].content = stageController.loadFxml("/fxml/tab_match_arrange.fxml")
        tabPane.tabs[3].content = stageController.loadFxml("/fxml/tab_match_history.fxml")
        tabPane.tabs[4].content = stageController.loadFxml("/fxml/tab_help_center.fxml")
    }

}