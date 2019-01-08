@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.stage_create_match
import cn.izis.base.stage_home
import cn.izis.bean.Match
import cn.izis.util.*
import cn.izis.util.rx.RxEvent
import com.jfoenix.controls.JFXTextField
import io.reactivex.Observable
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.stage.Modality
import javafx.stage.StageStyle
import java.net.URL
import java.util.*

class MatchInfoController : BaseController() {
    @FXML
    lateinit var tf_match_name: JFXTextField
    @FXML
    lateinit var tf_sponsor: JFXTextField
    @FXML
    lateinit var tf_match_address: JFXTextField
    @FXML
    lateinit var tf_match_referee: JFXTextField
    @FXML
    lateinit var tf_match_arrange: JFXTextField
    @FXML
    lateinit var tf_match_start_time: JFXTextField
    @FXML
    lateinit var tf_match_end_time: JFXTextField

    private var match: Match = Match()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        tf_match_name.textProperty().bind(match.matchNameProperty)
        tf_sponsor.textProperty().bind(match.sponsorProperty)
        tf_match_address.textProperty().bind(match.matchAddressProperty)
        tf_match_referee.textProperty().bind(match.matchRefereeProperty)
        tf_match_arrange.textProperty().bind(match.matchArrangeProperty)
        tf_match_start_time.textProperty().bind(match.matchTimeStartStringProperty)
        tf_match_end_time.textProperty().bind(match.matchTimeEndStringProperty)
        getMatch()
        subscribeEvent()
    }

    override fun onReceive(rxEvent: RxEvent) {
        super.onReceive(rxEvent)
        when(rxEvent.code){
            RxEvent.refreshMatchInfo ->{
                getMatch()
            }
        }
    }

    private fun getMatch(id: Int = 0) {
        Observable
            .create<Match> { emmit ->
                val result = queryMatch(id)
                result?.let {
                    emmit.onNext(it)
                }
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                match.copy(it)
            }
    }

    /**
     * 新建比赛
     */
    fun onCreateMatch(actionEvent: ActionEvent) {
        stageController?.loadAndShow(
            stage_home, stage_create_match, "/fxml/create_match.fxml", "新建比赛",
            StageStyle.UTILITY, Modality.APPLICATION_MODAL
        )
    }

    /**
     * 编辑比赛
     */
    fun onEditMatch(actionEvent: ActionEvent) {
        stageController?.loadAndShow(
            stage_home, stage_create_match, "/fxml/create_match.fxml", "编辑比赛",
            StageStyle.UTILITY, Modality.APPLICATION_MODAL
        )
        send(RxEvent.editMatch,match)
    }
}