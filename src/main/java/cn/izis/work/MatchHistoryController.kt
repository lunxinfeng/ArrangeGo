@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.stage_home
import cn.izis.bean.db.Match
import cn.izis.util.*
import cn.izis.util.rx.RxEvent
import io.reactivex.Observable
import javafx.scene.control.Alert
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import java.net.URL
import java.util.*

class MatchHistoryController : BaseController() {
    lateinit var tableView:TableView<Match>
    lateinit var tab_matchName:TableColumn<Match,String>
    lateinit var tab_sponsor:TableColumn<Match,String>
    lateinit var tab_referee:TableColumn<Match,String>
    lateinit var tab_arrange:TableColumn<Match,String>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        configTableView()

        subscribeEvent()

        getHistoryMatchs()
    }

    override fun onReceive(rxEvent: RxEvent) {
        super.onReceive(rxEvent)
        when(rxEvent.code){
            RxEvent.refreshMatchHistory -> getHistoryMatchs()
            RxEvent.del_match -> delMatch()
        }
    }

    private fun delMatch(){
        tableView.selectionModel.selectedItem?.let {
            if (it.match_id == matchCurr.match_id){
                toast("当前赛事不可删除")
            }else{
                hint("是否删除赛事：${it.match_name}?"){
                    val result = delMatch(it.match_id)
                    if (result > 0){
                        toast("删除赛事成功")
                        getHistoryMatchs()
                    }
                }
            }
        }
    }


    private fun configTableView() {
        tab_matchName.cellValueFactory = PropertyValueFactory<Match, String>("match_name")
        tab_sponsor.cellValueFactory = PropertyValueFactory<Match, String>("sponsor")
        tab_referee.cellValueFactory = PropertyValueFactory<Match, String>("match_referee")
        tab_arrange.cellValueFactory = PropertyValueFactory<Match, String>("match_arrange")

        tableView.setRowFactory {
            val row = TableRow<Match>()
            row.setOnMouseClicked {  event ->
                if (event.clickCount == 2 && !row.isEmpty){
                    info(row.item.match_name)
                    matchCurr.copy(row.item)
                    toast("切换赛事成功")
                }
            }
            row
        }
    }

    private fun getHistoryMatchs(){
        Observable
            .create<List<Match>> { emmit ->
                val result = queryAllMatchsWithOutRound()
                emmit.onNext(result)
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                tableView.items.clear()
                tableView.items.addAll(it)
            }
    }
}