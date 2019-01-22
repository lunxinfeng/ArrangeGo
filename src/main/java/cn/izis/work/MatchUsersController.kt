@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.stage_home
import cn.izis.bean.db.MatchUser
import cn.izis.util.*
import cn.izis.util.rx.RxEvent
import io.reactivex.Observable
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import java.net.URL
import java.util.*

class MatchUsersController: BaseController() {
    @FXML
    lateinit var table_users:TableView<MatchUser>
    lateinit var col_index:TableColumn<MatchUser,String>
    lateinit var col_name:TableColumn<MatchUser,String>
    lateinit var col_sex:TableColumn<MatchUser,String>
    lateinit var col_company:TableColumn<MatchUser,String>
    lateinit var col_phone:TableColumn<MatchUser,String>
    lateinit var col_age:TableColumn<MatchUser,String>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        configTableView()

        subscribeEvent()
    }

    private fun configTableView() {
        col_index.cellValueFactory = PropertyValueFactory<MatchUser, String>("index")
        col_name.cellValueFactory = PropertyValueFactory<MatchUser, String>("name")
        col_sex.cellValueFactory = PropertyValueFactory<MatchUser, String>("sex")
        col_company.cellValueFactory = PropertyValueFactory<MatchUser, String>("company")
        col_phone.cellValueFactory = PropertyValueFactory<MatchUser, String>("phone")
        col_age.cellValueFactory = PropertyValueFactory<MatchUser, String>("age")

        col_index.cellFactory = TextFieldTableCell.forTableColumn()
        col_name.cellFactory = TextFieldTableCell.forTableColumn()
        col_sex.cellFactory = TextFieldTableCell.forTableColumn()
        col_company.cellFactory = TextFieldTableCell.forTableColumn()
        col_phone.cellFactory = TextFieldTableCell.forTableColumn()
        col_age.cellFactory = TextFieldTableCell.forTableColumn()

        col_index.setOnEditCommit {
            it.rowValue.index = it.newValue
        }
        col_name.setOnEditCommit {
            it.rowValue.name = it.newValue
        }
        col_sex.setOnEditCommit {
            it.rowValue.sex = it.newValue
        }
        col_company.setOnEditCommit {
            it.rowValue.company = it.newValue
        }
        col_phone.setOnEditCommit {
            it.rowValue.phone = it.newValue
        }
        col_age.setOnEditCommit {
            it.rowValue.age = it.newValue
        }
    }

    override fun onReceive(rxEvent: RxEvent) {
        super.onReceive(rxEvent)
        when(rxEvent.code){
            RxEvent.refreshMatchUsers -> getUsers(matchCurr.match_id)
        }
    }

    private fun getUsers(matchId:Int){
        Observable
            .create<List<MatchUser>> { emmit ->
                val result = queryUsers(matchId)
                emmit.onNext(result)
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                table_users.items.clear()
                table_users.items.addAll(it)
            }
    }

    /**
     * 添加选手
     */
    fun onAddUser(actionEvent: ActionEvent) {
        table_users.items.add(MatchUser())
    }

    /**
     * 导入excel表
     */
    fun onImportExcel(actionEvent: ActionEvent) {
        val file = openDialog(
            owner = stageController?.getStage(stage_home),
            filters = *arrayOf(FILE_FILTER_EXCEL)
        )

        file?.let { f ->
            Observable
                .create<ObservableList<MatchUser>> {
                    val data = readUsersFromExcel(f)
                    it.onNext(data)
                    it.onComplete()
                }
                .compose(Transformer.io_main())
                .sub {
                    table_users.items.addAll(it)
                }

        }
    }

    /**
     * 保存设置
     */
    fun onSaveUsers(actionEvent: ActionEvent?) {
        Observable
            .create<Int> { emmit ->
                val result = saveUsers(1,table_users.items)
                emmit.onNext(result)
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                getUsers(matchCurr.match_id)
                toast("保存用户${if (it > 0) "成功" else "失败"}")
            }
    }

    /**
     * 导出到excel
     */
    fun onExportExcel(actionEvent: ActionEvent) {
        val file = saveDialog(
            owner = stageController?.getStage(stage_home),
            filters = *arrayOf(FILE_FILTER_EXCEL_XLS, FILE_FILTER_EXCEL_XLSX)
        )
        file?.let {
            Observable
                .create<Int> { emmit ->
                    saveUsersToExcel(file,table_users.items)
                    emmit.onComplete()
                }
                .compose(Transformer.io_main())
                .subscribe()
        }
    }

    /**
     * 编号
     */
    fun onArrange(actionEvent: ActionEvent) {
        val list = mutableListOf<Int>()
        for( i in 1..table_users.items.size){
            list.add(i)
        }

        list.shuffle()

        table_users.items.forEachIndexed { index, user ->
            user.index = list[index].toString()
        }

        table_users.refresh()

        onSaveUsers(null)
    }
}