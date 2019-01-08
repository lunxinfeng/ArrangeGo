package cn.izis.base

import cn.izis.util.JavaFxScheduler
import cn.izis.util.info
import cn.izis.util.rx.RxBus
import cn.izis.util.rx.RxEvent
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javafx.event.Event
import javafx.fxml.Initializable
import javafx.stage.Stage
import javafx.stage.WindowEvent


abstract class BaseController : Initializable, ControlledStage {
    internal var stageController: StageController? = null
    private var stage: Stage? = null
    private var stageName: String? = null
    internal var disposable: Disposable? = null

    override fun setStageController(stageController: StageController) {
        this.stageController = stageController
    }

    override fun setStage(stage: Stage, name: String) {
        this.stage = stage.apply {
            setOnCloseRequest {
                info("stage关闭了：$stageName")
                disposable?.dispose()
                stageController?.removeStage(name)
            }
        }
        this.stageName = name
    }

    fun getWindow() = stage

    fun closeSelf() = Event.fireEvent(getWindow(), WindowEvent(getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST))

    internal fun subscribeEvent() {
        RxBus.getDefault().toObservable(RxEvent::class.java)
            .observeOn(JavaFxScheduler.platform())
            .subscribe(object : Observer<RxEvent> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(rxEvent: RxEvent) {
                    onReceive(rxEvent)
                }

                override fun onError(throwable: Throwable) {
                    subscribeEvent()
                }

                override fun onComplete() {
                    subscribeEvent()
                }
            })
    }

    internal open fun onReceive(rxEvent: RxEvent) {
        info("onReceive：$rxEvent")
    }
}
