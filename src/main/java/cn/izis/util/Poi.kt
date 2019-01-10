package cn.izis.util

import cn.izis.bean.User
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory
import java.io.File
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.control.ProgressIndicator
import javafx.util.Callback
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.ss.usermodel.CellType
import java.io.FileOutputStream


fun readExcel(
        file:File
){
    val workBook = when{
        file.name.endsWith(".xls") -> HSSFWorkbookFactory.create(file.inputStream())
        file.name.endsWith(".xlsx") -> XSSFWorkbookFactory.create(file.inputStream())
        else -> null
    }

    workBook?.let { it ->
        it.all {//遍历sheet
            println(it.sheetName)
            it.rowIterator().forEach { row -> //遍历行
                println(row.rowNum)
                if (row.rowNum != 0) {//跳过第一行题头
                    row.cellIterator().forEach { cell -> //遍历每行的单元格
                        println(cell)
                    }
                }
            }

            true
        }
    }
}

fun saveExcel(
        file: File
){
    val workBook = XSSFWorkbookFactory.createWorkbook()
    val sheet = workBook.createSheet()
}

fun readUsersFromExcel(
    file:File
): ObservableList<User> {
    val workBook = when{
        file.name.endsWith(".xls") -> HSSFWorkbookFactory.create(file.inputStream())
        file.name.endsWith(".xlsx") -> XSSFWorkbookFactory.create(file.inputStream())
        else -> null
    }

    val data = FXCollections.observableArrayList<User>()
    workBook?.let { it ->
        //        it.all { sheet ->//遍历sheet
        it.first { sheet ->//第一个sheet
            sheet.rowIterator().forEach { row -> //遍历行
                if (row.rowNum == 0){ //第一行，题头

                }else{
                    val user = User()
                    row.cellIterator().forEach { cell -> //遍历每行的单元格
                        //rowIndex columnIndex都从0开始
                        user.index = cell.rowIndex.toString()
                        when (cell.columnIndex){
                            0 -> user.name = cell.stringCellValue
                            1 -> user.sex = cell.stringCellValue
                            2 -> user.company = cell.stringCellValue
                            3 -> user.phone = cell.stringCellValue
                            4 -> user.age = cell.stringCellValue
                        }
                    }
                    data.add(user)
                }
            }
            true
        }
    }
    return data
}

fun saveUsersToExcel(
    file: File,
    data: List<User>
){
    if (!file.exists())
        file.createNewFile()
    val workBook = when{
        file.name.endsWith(".xls") -> HSSFWorkbookFactory.createWorkbook()
        file.name.endsWith(".xlsx") -> XSSFWorkbookFactory.createWorkbook()
        else -> null
    }
    workBook?.let {
        val sheet = workBook.createSheet("参赛名单")
        val rowTitle = sheet.createRow(0)
        val titles = arrayOf("姓名","性别","年龄","联系方式","单位")
        for ((index, t_name) in titles.withIndex()){
            rowTitle.createCell(index).apply {
                cellType = CellType.STRING
                setCellValue(t_name)
            }
        }
        data.forEachIndexed { index, user ->
            val row = sheet.createRow(index + 1)
            for (col_num in 0 .. titles.size){
                row.createCell(col_num).apply {
                    cellType = CellType.STRING
                    when(col_num){
                        0 -> setCellValue(user.name)
                        1 -> setCellValue(user.sex)
                        2 -> setCellValue(user.age)
                        3 -> setCellValue(user.phone)
                        4 -> setCellValue(user.company)
                    }
                }
            }
        }

        val outputStream = FileOutputStream(file)
        workBook.write(outputStream)
        outputStream.close()
        workBook.close()
    }
}




/**
 * 动态生成列
 */
fun readExcelToTableViewD(
    file:File,
    tableView:TableView<ObservableList<StringProperty>>
){
    tableView.placeholder = ProgressIndicator()
    val workBook = when{
        file.name.endsWith(".xls") -> HSSFWorkbookFactory.create(file.inputStream())
        file.name.endsWith(".xlsx") -> XSSFWorkbookFactory.create(file.inputStream())
        else -> null
    }

    workBook?.let { it ->
//        it.all { sheet ->//遍历sheet
        it.first { sheet ->//第一个sheet
            val data = FXCollections.observableArrayList<ObservableList<StringProperty>>()
            sheet.rowIterator().forEach { row -> //遍历行
                if (row.rowNum == 0){ //第一行，题头
                    row.cellIterator().forEach { cell -> //遍历每行的单元格
                        tableView.columns.add(createColumn(cell.columnIndex,cell.stringCellValue))

                        Unit
                    }
                }else{
                    val rowData = FXCollections.observableArrayList<StringProperty>()
//                    rowData.add(SimpleStringProperty(row.rowNum.toString()))
                    row.cellIterator().forEach { cell -> //遍历每行的单元格
                        println(cell)
                        rowData.add(SimpleStringProperty(cell.stringCellValue))
                    }
                    data.add(rowData)
                }
            }
            tableView.items = data

            true
        }
    }
}

private fun createColumn(
    columnIndex: Int,
    columnTitle: String?
): TableColumn<ObservableList<StringProperty>, String> {
    val column = TableColumn<ObservableList<StringProperty>, String>()
    val title: String = if (columnTitle == null || columnTitle.trim { it <= ' ' }.isEmpty()) {
        "Column " + (columnIndex + 1)
    } else {
        columnTitle
    }
    column.text = title
    column.cellValueFactory =
            Callback<TableColumn.CellDataFeatures<ObservableList<StringProperty>, String>, ObservableValue<String>> { cellDataFeatures ->
                val values = cellDataFeatures.value
                if (columnIndex >= values.size) {
                    SimpleStringProperty("")
                } else {
                    cellDataFeatures.value[columnIndex]
                }
            }
    return column
}