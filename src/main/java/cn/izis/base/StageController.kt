package cn.izis.base

import cn.izis.util.info
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle

interface ControlledStage {
    fun setStageController(stageController: StageController)

    fun setStage(stage: Stage, name: String)
}

class StageController {
    private val stages = HashMap<String, Stage>()

    fun addStage(name: String, stage: Stage) {
        stages[name] = stage

        info(stages.keys.toString())
    }

    fun removeStage(name: String){
        stages.remove(name)
        info("removeStage $name")
    }


    fun getStage(name: String): Stage? {
        return stages[name]
    }

    fun loadFxml(resource: String): Pane? {
        val loader = FXMLLoader(javaClass.getResource(resource))

        val pane = loader.load<Pane>()
        info("load fxml:$resource")
        val controlledStage = loader.getController<ControlledStage>()
        controlledStage.setStageController(this)
        return pane
    }

    fun loadStage(name: String, resource: String, title: String, style: StageStyle = StageStyle.DECORATED, modality: Modality = Modality.NONE) {
        if (getStage(name) != null) return

        val loader = FXMLLoader(javaClass.getResource(resource))
        val pane = loader.load<Pane>()
        info("load fxml:$name")

        //通过Loader获取FXML对应的Controller，所有Controller实现ControlledStage接口，并将本StageController注入到Controller中
        val controlledStage = loader.getController<ControlledStage>()
        controlledStage.setStageController(this)

        //构造对应的Stage
        val scene = Scene(pane)
        val stage = Stage()
        stage.title = title
        stage.scene = scene.apply {
            stylesheets.add("/css/base.css")
        }
        controlledStage.setStage(stage, name)

        //配置
        stage.initStyle(style)
        stage.initModality(modality)


        //将设置好的Stage放到HashMap中
        this.addStage(name, stage)
    }

    fun showStage(name: String, owner: String? = null) {
        if (getStage(name) == null) return

        if (getStage(name)!!.isShowing) {
            getStage(name)!!.toFront()
        } else {
            if (getStage(name)!!.owner == null && owner != null) {
                getStage(name)!!.initOwner(getStage(owner))
                if (getStage(owner)!!.isMaximized)
                    getStage(name)!!.isMaximized = true
                getStage(name)!!.maximizedProperty()?.addListener { _, _, newValue -> getStage(owner)!!.isMaximized = newValue!! }
            }

            getStage(name)!!.show()
        }
    }

    fun loadAndShow(owner: String? = null, name: String, resource: String, title: String, style: StageStyle = StageStyle.DECORATED, modality: Modality = Modality.NONE) {
        loadStage(name, resource, title, style,modality)
        showStage(name, owner)
    }
}