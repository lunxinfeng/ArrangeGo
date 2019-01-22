@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.Api
import cn.izis.base.BaseController
import cn.izis.bean.GameInfo
import cn.izis.bean.db.MatchRound
import cn.izis.bean.db.MatchUser
import cn.izis.util.*
import cn.izis.util.rx.RxEvent
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXListView
import com.lxf.rxretrofit.RetrofitHelper
import io.reactivex.Observable
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

class MatchArrangeController : BaseController() {
    lateinit var listViewRound: JFXListView<MatchRound>
    lateinit var tableViewRound: TableView<GameInfo>
    lateinit var tab_num: TableColumn<GameInfo, String>
    lateinit var tab_black: TableColumn<GameInfo, String>
    lateinit var tab_total_score_black: TableColumn<GameInfo, Int>
    lateinit var tab_score_black: TableColumn<GameInfo, Int>
    lateinit var tab_status: TableColumn<GameInfo, String>
    lateinit var tab_score_white: TableColumn<GameInfo, Int>
    lateinit var tab_total_score_white: TableColumn<GameInfo, Int>
    lateinit var tab_white: TableColumn<GameInfo, String>
    lateinit var btnArrange: JFXButton
    lateinit var btnWin: JFXButton
    lateinit var btnLose: JFXButton
    lateinit var btnPing: JFXButton
    lateinit var btnFinish: JFXButton

    private val matchUsers = mutableListOf<MatchUser>()
    private val hasEmpty = SimpleBooleanProperty(false)

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        configListView()
        configTableView()

        val notEdit = Bindings.or(Bindings.or(btnFinish.disableProperty(),tableViewRound.selectionModel.selectedItemProperty().isNull),hasEmpty)

        btnArrange.disableProperty().bind(Bindings.isNotEmpty(tableViewRound.items))
        btnWin.disableProperty().bind(notEdit)
        btnLose.disableProperty().bind(notEdit)
        btnPing.disableProperty().bind(notEdit)

        subscribeEvent()
    }

    private fun configListView() {
        listViewRound.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            tableViewRound.items.clear()
            newValue?.let{
                getGameInfos(matchCurr.match_id, it.roundIndex)
                btnFinish.isDisable = it.status == 1
            }
        }
    }

    private fun configTableView() {
        tab_num.cellValueFactory = PropertyValueFactory<GameInfo, String>("num")
        tab_black.cellValueFactory = PropertyValueFactory<GameInfo, String>("blackName")
        tab_total_score_black.cellValueFactory = PropertyValueFactory<GameInfo, Int>("blackTotalScore")
        tab_score_black.cellValueFactory = PropertyValueFactory<GameInfo, Int>("blackScore")
        tab_status.cellValueFactory = PropertyValueFactory<GameInfo, String>("status")
        tab_score_white.cellValueFactory = PropertyValueFactory<GameInfo, Int>("whiteScore")
        tab_total_score_white.cellValueFactory = PropertyValueFactory<GameInfo, Int>("whiteTotalScore")
        tab_white.cellValueFactory = PropertyValueFactory<GameInfo, String>("whiteName")

        tableViewRound.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            newValue?.let{
                hasEmpty.set(it.whiteId == 9191)
            }
        }
    }

    override fun onReceive(rxEvent: RxEvent) {
        super.onReceive(rxEvent)
        when (rxEvent.code) {
            RxEvent.refreshMatchArrange -> {
                getRounds(matchCurr.match_id)
                getMatchUsers(matchCurr.match_id)
                listViewRound.selectionModel.selectedItem?.let {
                    getGameInfos(matchCurr.match_id, it.roundIndex)
                }
            }
        }
    }

    private fun getRounds(matchId: Int) {
        Observable
            .create<List<MatchRound>> { emmit ->
                val result = queryRoundsByMatchId(matchId)
                emmit.onNext(result)
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                listViewRound.items = FXCollections.observableArrayList(it)
                listViewRound.selectionModel.select(0)
            }
    }

    private fun getMatchUsers(matchId: Int) {
        Observable
            .create<List<MatchUser>> { emmit ->
                val result = queryUsers(matchId)
                emmit.onNext(result)
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                matchUsers.clear()
                matchUsers.addAll(it)
            }
    }

    private fun getGameInfos(matchId: Int, roundIndex: Int) {
        Observable
            .create<List<GameInfo>> { emmit ->
                val result = queryGameInfo(matchId, roundIndex)
                emmit.onNext(result)
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                val selectIndex = tableViewRound.selectionModel.selectedIndex

                tableViewRound.items.clear()
                tableViewRound.items.addAll(it)

                if (selectIndex + 1 < tableViewRound.items.size)
                    tableViewRound.selectionModel.select(selectIndex + 1)
                else
                    tableViewRound.selectionModel.select(selectIndex)
                tableViewRound.requestFocus()
            }
    }

    /**
     * 自动编排
     */
    fun onArrange(actionEvent: ActionEvent) {
        arrange(matchCurr.match_id,listViewRound.selectionModel.selectedItem.roundIndex, matchUsers)
        getGameInfos(matchCurr.match_id, listViewRound.selectionModel.selectedItem.roundIndex)
    }

    /**
     * 结束本轮
     */
    fun onFinishRound(actionEvent: ActionEvent) {
        RetrofitHelper.getInstance()
            .baseUrl("http://apitest.yqlwq.cn")
            .debug(true)
            .create(Api::class.java)
            .modifyPassword(HashMap<String, String>().apply {
                put("old_password", "123456")
                put("password", "234567")
                put("password_confirmation", "234567")
                put(
                    "token",
                    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vYXBpdGVzdC55cWx3cS5jbi9hcGkvZ2FtZS91c2VyL2xvZ2luIiwiaWF0IjoxNTQ3Njk2OTY4LCJleHAiOjE1NDc3MDA1NjgsIm5iZiI6MTU0NzY5Njk2OCwianRpIjoiNklkVWNtcFlkSTRNb0Y1bCIsInN1YiI6NjcsInBydiI6IjViYTE5ZjAyODY5MTY0Y2M2MWJjNzk2NjY3ZGUxYjYwMjE4ZWVjMjUifQ.xl0zG4O0MKRAXCk5Hs5WVvBji22tnRIiCHXbIELeW3E"
                )
            })
            .compose(Transformer.io_main())
            .sub {
                info(it.toString())
                info(it.body().toString())
                info(it.errorBody()?.string() ?: "error body")
            }
    }

    /**
     * 设置结果 胜
     */
    fun onWin(actionEvent: ActionEvent) {
        val matchId = matchCurr.match_id
        val roundIndex = listViewRound.selectionModel.selectedItem.roundIndex
        val gameInfo = tableViewRound.selectionModel.selectedItem.apply {
            //多次点击，先复原数据
            blackTotalScore -= blackScore
            whiteTotalScore -= whiteScore
            blackScore = 0
            whiteScore = 0

            blackScore = 2
            blackTotalScore += blackScore
            status = "胜"
        }
        updateResult(matchId, roundIndex, gameInfo)
    }

    /**
     * 设置结果 负
     */
    fun onLose(actionEvent: ActionEvent) {
        val matchId = matchCurr.match_id
        val roundIndex = listViewRound.selectionModel.selectedItem.roundIndex
        val gameInfo = tableViewRound.selectionModel.selectedItem.apply {
            //多次点击，先复原数据
            blackTotalScore -= blackScore
            whiteTotalScore -= whiteScore
            blackScore = 0
            whiteScore = 0

            whiteScore = 2
            whiteTotalScore += whiteScore
            status = "负"
        }

        updateResult(matchId, roundIndex, gameInfo)
    }

    /**
     * 设置结果 和
     */
    fun onPingJu(actionEvent: ActionEvent) {
        val matchId = matchCurr.match_id
        val roundIndex = listViewRound.selectionModel.selectedItem.roundIndex
        val gameInfo = tableViewRound.selectionModel.selectedItem.apply {
            //多次点击，先复原数据
            blackTotalScore -= blackScore
            whiteTotalScore -= whiteScore
            blackScore = 0
            whiteScore = 0

            blackScore = 1
            blackTotalScore += blackScore
            whiteScore = 1
            whiteTotalScore += whiteScore
            status = "和"
        }
        updateResult(matchId, roundIndex, gameInfo)
    }

    //设置结果后更新本地数据库和显示
    private fun updateResult(matchId: Int, roundIndex: Int, gameInfo: GameInfo) {
        Observable
            .create<Int> { emmit ->
                val result = updateMatchArrange(matchId, roundIndex, gameInfo)
                emmit.onNext(result)
                emmit.onComplete()
            }
            .flatMap {
                Observable
                    .create<Int> { emmit ->
                        val result = updateMatchGame(matchId, roundIndex, gameInfo)
                        emmit.onNext(result)
                        emmit.onComplete()
                    }
            }
            .compose(Transformer.io_main())
            .sub {
                getGameInfos(matchId, roundIndex)
            }
    }

}