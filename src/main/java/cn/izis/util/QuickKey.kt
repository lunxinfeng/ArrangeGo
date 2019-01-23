package cn.izis.util

import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination

typealias QuickKeyListener = () -> Unit

fun Scene.quickKey(keyCode: KeyCode,vararg modifiers:KeyCombination.Modifier, onQuickKey:QuickKeyListener){
    val keyCombination = KeyCodeCombination(keyCode)
    this.accelerators[keyCombination] = Runnable { onQuickKey.invoke() }
}