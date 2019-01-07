package cn.izis

import cn.izis.base.StageController
import cn.izis.base.stage_create_match
import cn.izis.base.stage_home
import javafx.application.Application
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle

import java.util.Locale


class App : Application() {
    private val stageController = StageController()
    override fun start(stage: Stage) {
        Locale.setDefault(Locale.CHINA)
        stageController.loadAndShow(null,stage_home, "/fxml/home.fxml", "赛事编排")
    }
}
