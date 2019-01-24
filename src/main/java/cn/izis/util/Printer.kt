package cn.izis.util

import javafx.print.PageOrientation
import javafx.print.Paper
import javafx.print.Printer
import javafx.print.PrinterJob
import javafx.scene.Node
import javafx.scene.transform.Scale



fun printNode(node:Node){
    val printer = Printer.getDefaultPrinter()
    val pageLayout = printer.createPageLayout(Paper.A4,PageOrientation.PORTRAIT,Printer.MarginType.HARDWARE_MINIMUM)

    info("A4： ${pageLayout.printableWidth} \t ${pageLayout.printableHeight} \t $pageLayout")
    info("Node： ${node.boundsInParent}")

    val attr = printer.printerAttributes
    val scaleX = (pageLayout.printableWidth - 50) / node.boundsInParent.width
    val scaleY = (pageLayout.printableHeight - 50) / node.boundsInParent.height
    val scale = Scale(scaleX, scaleY)
    node.transforms.add(scale)

    val printJob = PrinterJob.createPrinterJob(printer)

    printJob?.let {
        printJob.jobSettings.pageLayout = pageLayout
//        if (it.showPageSetupDialog(node.scene.window)){
            if (it.showPrintDialog(node.scene.window)){
                if (it.printPage(node))
                    it.endJob()
            }
//        }
    }
    node.transforms.remove(scale)
}

fun main(args: Array<String>) {
    //获取电脑上安装的所有打印机
    val printers = Printer.getAllPrinters()
    println(printers) //[Printer Fax, Printer HP LaserJet Professional M1136 MFP, Printer Microsoft Print to PDF, Printer Microsoft XPS Document Writer]

    //获取默认打印机
    val printer = Printer.getDefaultPrinter()
    println(printer) //Printer Microsoft Print to PDF

    val attr = printer.printerAttributes
    println(attr)

    val printerJob = PrinterJob.createPrinterJob(printer)
    println(printerJob)

    val jobSettings = printerJob.jobSettings
    println(jobSettings)
}