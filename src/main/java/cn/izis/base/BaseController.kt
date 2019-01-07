package cn.izis.base

import javafx.event.Event
import javafx.fxml.Initializable
import javafx.stage.Stage
import javafx.stage.WindowEvent


abstract class BaseController : Initializable, ControlledStage {
    internal var stageController: StageController? = null
    private var stage: Stage? = null
    private var stageName: String? = null

    override fun setStageController(stageController: StageController) {
        this.stageController = stageController
    }

    override fun setStage(stage: Stage, name: String) {
        this.stage = stage
        this.stageName = name
    }

    fun getWindow() = stage

    fun closeSelf() = Event.fireEvent(getWindow(), WindowEvent(getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST))
}
