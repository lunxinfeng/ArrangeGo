package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.StageController
import com.jfoenix.controls.JFXTabPane
import javafx.fxml.FXML
import java.net.URL
import java.util.*

class HomeController:BaseController() {
    @FXML lateinit var tabPane:JFXTabPane

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

    override fun setStageController(stageController: StageController) {
        super.setStageController(stageController)
        tabPane.tabs[0].content = stageController.loadFxml("/fxml/tab_match_info.fxml")
    }

}