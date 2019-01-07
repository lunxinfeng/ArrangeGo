package cn.izis.controller

import cn.izis.base.BaseController
import cn.izis.bean.Match
import cn.izis.util.*
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXDatePicker
import com.jfoenix.controls.JFXTextField
import com.jfoenix.controls.JFXTimePicker
import com.lxf.rxretrofit.callback.BaseView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javafx.event.ActionEvent
import javafx.fxml.FXML
import java.net.URL
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class MatchCreateController : BaseController() {
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

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        btnConfirm.disableProperty().bind(tf_match_name.textProperty().isEmpty)

        date_match_time_start.value = LocalDate.now()
        time_match_time_start.value = LocalTime.now()
        date_match_time_end.value = LocalDate.now()
        time_match_time_end.value = LocalTime.now()
    }

    @FXML
    fun onConfirm(actionEvent: ActionEvent) {
        Observable
            .create<Int> {
                val result = createMatch(
                    Match(
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
                        tf_match_arrange.text
                    )
                )
                it.onNext(result)
                it.onComplete()
            }
            .compose(Transformer.io_main())
            .sub{
                if (it > 0) {
                    toast("创建比赛成功")
                    closeSelf()
                }
            }
    }

    fun onCancel(actionEvent: ActionEvent) {
        closeSelf()
    }
}