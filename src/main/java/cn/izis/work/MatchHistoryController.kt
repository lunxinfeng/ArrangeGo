@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.bean.Match
import cn.izis.util.Transformer
import cn.izis.util.queryAllMatchsWithOutRound
import cn.izis.util.rx.RxEvent
import cn.izis.util.sub
import io.reactivex.Observable
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
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
        }
    }


    private fun configTableView() {
        tab_matchName.cellValueFactory = PropertyValueFactory<Match, String>("match_name")
        tab_sponsor.cellValueFactory = PropertyValueFactory<Match, String>("sponsor")
        tab_referee.cellValueFactory = PropertyValueFactory<Match, String>("match_referee")
        tab_arrange.cellValueFactory = PropertyValueFactory<Match, String>("match_arrange")
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