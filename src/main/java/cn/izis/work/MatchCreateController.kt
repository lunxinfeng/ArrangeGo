@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.bean.db.Match
import cn.izis.util.*
import cn.izis.util.rx.RxEvent
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXDatePicker
import com.jfoenix.controls.JFXTextField
import com.jfoenix.controls.JFXTimePicker
import io.reactivex.Observable
import javafx.event.ActionEvent
import javafx.fxml.FXML
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

open class MatchCreateController : BaseController() {
    @FXML
    lateinit var tf_match_name: JFXTextField
    @FXML
    lateinit var tf_sponsor: JFXTextField
    @FXML
    lateinit var tf_organizer: JFXTextField
    @FXML
    lateinit var tf_co_organizer: JFXTextField
    @FXML
    lateinit var tf_supporting: JFXTextField
    @FXML
    lateinit var tf_match_arbitration: JFXTextField
    @FXML
    lateinit var tf_match_address: JFXTextField
    @FXML
    lateinit var tf_match_referee: JFXTextField
    @FXML
    lateinit var tf_match_arrange: JFXTextField
    @FXML
    lateinit var date_match_time_start: JFXDatePicker
    @FXML
    lateinit var time_match_time_start: JFXTimePicker
    @FXML
    lateinit var date_match_time_end: JFXDatePicker
    @FXML
    lateinit var time_match_time_end: JFXTimePicker
    @FXML
    lateinit var btnConfirm: JFXButton

    /**
     * 上页面传过来的数据
     */
    private var matchPre: Match? = null
    /**
     * 本页面编辑的数据
     */
    private val matchEdit
        get() = Match(
            tf_match_name.text,
            tf_sponsor.text,
            tf_organizer.text,
            tf_co_organizer.text,
            tf_supporting.text,
            tf_match_arbitration.text,
            tf_match_address.text,
            getTime(date_match_time_start.value, time_match_time_start.value),
            getTime(date_match_time_end.value, time_match_time_end.value),
            tf_match_referee.text,
            tf_match_arrange.text,
            matchPre?.match_id ?: 0
        )

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        btnConfirm.disableProperty().bind(tf_match_name.textProperty().isEmpty)

        date_match_time_start.value = LocalDate.now()
        time_match_time_start.value = LocalTime.now()
        date_match_time_end.value = LocalDate.now()
        time_match_time_end.value = LocalTime.now()

        subscribeEvent()
    }


    override fun onReceive(rxEvent: RxEvent) {
        super.onReceive(rxEvent)
        when (rxEvent.code) {
            RxEvent.editMatch -> {
                matchPre = rxEvent.`object` as Match

                tf_match_name.text = matchPre?.match_name
                tf_sponsor.text = matchPre?.sponsor
                tf_organizer.text = matchPre?.organizer
                tf_co_organizer.text = matchPre?.co_organizer
                tf_supporting.text = matchPre?.supporting
                tf_match_arbitration.text = matchPre?.match_arbitration
                tf_match_address.text = matchPre?.match_address
                tf_match_referee.text = matchPre?.match_referee
                tf_match_arrange.text = matchPre?.match_arrange
                date_match_time_start.value = matchPre?.match_time_start?.toLocalDateTime()?.toLocalDate()
                time_match_time_start.value = matchPre?.match_time_start?.toLocalDateTime()?.toLocalTime()
                date_match_time_end.value = matchPre?.match_time_end?.toLocalDateTime()?.toLocalDate()
                time_match_time_end.value = matchPre?.match_time_end?.toLocalDateTime()?.toLocalTime()
            }
        }
    }

    private fun create(){
        Observable
            .create<Int> {
                val result = createMatch(matchEdit)
                it.onNext(result)
                it.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                if (it > 0) {
                    toast("创建比赛成功")
                    matchCurr.match_id = 0
                    send(RxEvent.refreshMatchInfo)
                    closeSelf()
                }
            }
    }

    private fun update() {
        Observable
            .create<Int> {
                val result = updateMatch(matchEdit)
                it.onNext(result)
                it.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                if (it > 0) {
                    toast("更新比赛信息成功")
                    send(RxEvent.refreshMatchInfo)
                    closeSelf()
                }
            }
    }

    @FXML
    open fun onConfirm(actionEvent: ActionEvent) {
        if (matchPre == null)
            create()
        else
            update()
    }

    @FXML
    fun onCancel(actionEvent: ActionEvent) {
        closeSelf()
    }
}