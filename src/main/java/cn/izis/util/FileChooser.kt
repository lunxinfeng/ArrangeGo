package cn.izis.util

import javafx.stage.FileChooser
import javafx.stage.Window
import java.io.File

val FILE_FILTER_EXCEL = FileChooser.ExtensionFilter("excel","*.xls","*.xlsx")
val FILE_FILTER_EXCEL_XLS = FileChooser.ExtensionFilter("excel","*.xls")
val FILE_FILTER_EXCEL_XLSX = FileChooser.ExtensionFilter("excel","*.xlsx")

fun openDialog(
        owner: Window? = null,
        title:String? = null,
        vararg filters:FileChooser.ExtensionFilter
): File? {
    val fileDialog = FileChooser()
    title?.let { fileDialog.title = title }

    fileDialog.extensionFilters.addAll(filters)
    return fileDialog.showOpenDialog(owner)
}

fun saveDialog(
    owner: Window? = null,
    title:String? = null,
    vararg filters:FileChooser.ExtensionFilter
): File? {
    val fileDialog = FileChooser()
    title?.let { fileDialog.title = title }

    fileDialog.extensionFilters.addAll(filters)
    return fileDialog.showSaveDialog(owner)
}