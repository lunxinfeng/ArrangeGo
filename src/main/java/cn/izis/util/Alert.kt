package cn.izis.util

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType

fun hint(message:String,apply:() -> Unit){
    val alert = Alert(Alert.AlertType.WARNING,message, ButtonType.APPLY, ButtonType.CANCEL)
    when(alert.showAndWait().get()){
        ButtonType.APPLY -> apply.invoke()
    }
}