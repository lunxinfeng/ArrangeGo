package cn.izis.util

import org.apache.poi.hssf.usermodel.HSSFWorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory
import java.io.File

fun readExcel(
        file:File
){
    val workBook = when{
        file.name.endsWith(".xls") -> HSSFWorkbookFactory.create(file.inputStream())
        file.name.endsWith(".xlsx") -> XSSFWorkbookFactory.create(file.inputStream())
        else -> null
    }

    workBook?.let { it ->
        it.all {
            println(it.sheetName)
            it.rowIterator().forEach { row ->
                row.cellIterator().forEach { cell ->
                    println(cell)
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