package cn.izis.util

import javafx.stage.FileChooser
import javafx.stage.Window
import java.io.File

val FILE_FILTER_EXCEL = FileChooser.ExtensionFilter("excel","*.xls","*.xlsx")

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