package cn.izis.controller

import cn.izis.base.BaseController
import cn.izis.base.stage_create_match
import cn.izis.base.stage_home
import javafx.event.ActionEvent
import javafx.stage.Modality
import javafx.stage.StageStyle
import java.net.URL
import java.util.*

class HomeController:BaseController() {
    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }

    /**
     * 新建比赛
     */
    fun onCreateMatch(actionEvent: ActionEvent) {
        stageController?.loadAndShow(stage_home, stage_create_match, "/fxml/create_match.fxml", "新建比赛",
            StageStyle.UTILITY,Modality.APPLICATION_MODAL)
    }
}