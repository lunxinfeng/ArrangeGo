package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.StageController
import cn.izis.util.rx.RxEvent
import cn.izis.util.send
import com.jfoenix.controls.JFXTabPane
import javafx.fxml.FXML
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
    }

}