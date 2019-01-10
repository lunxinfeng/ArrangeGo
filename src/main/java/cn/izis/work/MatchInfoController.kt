@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.stage_create_match
import cn.izis.base.stage_home
import cn.izis.bean.Match
import cn.izis.bean.MatchRound
import cn.izis.util.*
import cn.izis.util.rx.RxEvent
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXDatePicker
import com.jfoenix.controls.JFXTextField
import com.jfoenix.controls.JFXTimePicker
import io.reactivex.Observable
import javafx.beans.binding.Bindings
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.HBox
import javafx.stage.Modality
import javafx.stage.StageStyle
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

/**
 * 当前展示的赛事
 */
var matchCurr: Match = Match()

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
    @FXML
    lateinit var btnEditMatch: JFXButton
    @FXML
    lateinit var btnAddRound: JFXButton
    @FXML
    lateinit var btnDelRound: JFXButton
    @FXML
    lateinit var table_round: TableView<MatchRound>
    @FXML
    lateinit var col_round: TableColumn<MatchRound, Int>
    @FXML
    lateinit var col_time: TableColumn<MatchRound, Long?>



    override fun initialize(location: URL?, resources: ResourceBundle?) {
        table_round.widthProperty().addListener { _, _, newValue ->
            col_round.minWidth = newValue.toDouble() * 0.15
            col_time.minWidth = newValue.toDouble() * 0.8
        }
        col_round.cellValueFactory = PropertyValueFactory<MatchRound, Int>("roundIndex")
        col_time.cellValueFactory = PropertyValueFactory<MatchRound, Long?>("time_start")
        col_time.setCellFactory { MyCell() }

        table_round.items = matchCurr.match_round_list
        tf_match_name.textProperty().bind(matchCurr.matchNameProperty)
        tf_sponsor.textProperty().bind(matchCurr.sponsorProperty)
        tf_match_address.textProperty().bind(matchCurr.matchAddressProperty)
        tf_match_referee.textProperty().bind(matchCurr.matchRefereeProperty)
        tf_match_arrange.textProperty().bind(matchCurr.matchArrangeProperty)
        tf_match_start_time.textProperty().bind(matchCurr.matchTimeStartStringProperty)
        tf_match_end_time.textProperty().bind(matchCurr.matchTimeEndStringProperty)

        btnEditMatch.disableProperty().bind(matchCurr.matchNameProperty.isNull)
        btnAddRound.disableProperty().bind(matchCurr.matchNameProperty.isNull)
        btnDelRound.disableProperty().bind(Bindings.isEmpty(matchCurr.match_round_list))

        getMatch()
        subscribeEvent()
    }

    override fun onReceive(rxEvent: RxEvent) {
        super.onReceive(rxEvent)
        when (rxEvent.code) {
            RxEvent.refreshMatchInfo -> {
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
                matchCurr.copy(it)
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
        send(RxEvent.editMatch, matchCurr)
    }

    /**
     * 添加轮次
     */
    fun onAddRound(actionEvent: ActionEvent) {
        val size = matchCurr.match_round_list.size
        matchCurr.match_round_list.add(MatchRound(size + 1, matchCurr.match_id))
    }

    /**
     * 删除最后一轮
     */
    fun onDeleteRound(actionEvent: ActionEvent) {
        val size = matchCurr.match_round_list.size
        matchCurr.match_round_list.removeAt(size - 1)
    }

    /**
     * 保存轮次
     */
    fun onSaveRound(actionEvent: ActionEvent) {
        Observable
            .create<Int> { emmit ->
                val result = saveMatchRound(matchCurr)
                emmit.onNext(result)
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                toast("保存设置${if (it > 0) "成功" else "失败"}")
            }
    }

    inner class MyCell : TableCell<MatchRound, Long?>() {
        override fun updateItem(item: Long?, empty: Boolean) {
            super.updateItem(item, empty)
            graphic = if (empty)
                null
            else {
                val date = JFXDatePicker()
                val time = JFXTimePicker()
                date.apply {
                    value = item?.toLocalDateTime()?.toLocalDate() ?: LocalDate.now()
                    valueProperty().addListener { _, _, newValue ->
                        matchCurr.match_round_list[index].time_start = getTime(newValue,time.value)
                    }
                }
                time.apply {
                    value = item?.toLocalDateTime()?.toLocalTime() ?: LocalTime.now()
                    valueProperty().addListener { _, _, newValue ->
                        matchCurr.match_round_list[index].time_start = getTime(date.value,newValue)
                    }
                }

                HBox().apply {
                    alignment = Pos.CENTER
                    children.addAll(date, time)
                }
            }
        }
    }
}