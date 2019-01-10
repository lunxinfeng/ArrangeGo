@file:Suppress("PropertyName")

package cn.izis.work

import cn.izis.base.BaseController
import cn.izis.base.stage_home
import cn.izis.bean.User
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
    lateinit var table_users:TableView<User>
    lateinit var col_index:TableColumn<User,String>
    lateinit var col_name:TableColumn<User,String>
    lateinit var col_sex:TableColumn<User,String>
    lateinit var col_company:TableColumn<User,String>
    lateinit var col_phone:TableColumn<User,String>
    lateinit var col_age:TableColumn<User,String>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        col_index.cellValueFactory = PropertyValueFactory<User,String>("index")
        col_name.cellValueFactory = PropertyValueFactory<User,String>("name")
        col_sex.cellValueFactory = PropertyValueFactory<User,String>("sex")
        col_company.cellValueFactory = PropertyValueFactory<User,String>("company")
        col_phone.cellValueFactory = PropertyValueFactory<User,String>("phone")
        col_age.cellValueFactory = PropertyValueFactory<User,String>("age")

        col_index.cellFactory = TextFieldTableCell.forTableColumn()
        col_name.cellFactory = TextFieldTableCell.forTableColumn()
        col_sex.cellFactory = TextFieldTableCell.forTableColumn()
        col_company.cellFactory = TextFieldTableCell.forTableColumn()
        col_phone.cellFactory = TextFieldTableCell.forTableColumn()
        col_age.cellFactory = TextFieldTableCell.forTableColumn()

        subscribeEvent()
    }

    override fun onReceive(rxEvent: RxEvent) {
        super.onReceive(rxEvent)
        when(rxEvent.code){
            RxEvent.refreshMatchUsers -> getUsers(matchCurr.match_id)
        }
    }

    private fun getUsers(matchId:Int){
        Observable
            .create<List<User>> { emmit ->
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
        table_users.items.add(User())
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
                .create<ObservableList<User>> {
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
    fun onSaveUsers(actionEvent: ActionEvent) {
        Observable
            .create<Int> { emmit ->
                val result = addUser(1,table_users.items)
                emmit.onNext(result)
                emmit.onComplete()
            }
            .compose(Transformer.io_main())
            .sub {
                toast("保存用户${if (it > 0) "成功" else "失败"}")
            }
    }

    /**
     * 导出到excel
     */
    fun onExportExcel(actionEvent: ActionEvent) {

    }

    /**
     * 编排
     */
    fun onArrange(actionEvent: ActionEvent) {

    }
}