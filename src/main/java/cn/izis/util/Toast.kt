package cn.izis.util

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import java.util.*

fun toast(
    message: String,
    ownerStage: Stage? = null,
    backgroundColor: Paint = Color.color(0.0,0.0,0.0,0.7),
    backgroundRadius: Double = 10.0,
    textColor: Paint = Color.WHITE,
    textFont: Font = Font.font(20.0),
    toastDelay: Long = 1200,
    inDelay: Double = 500.0,
    outDelay: Double = 500.0
) = Toast.show(ownerStage, message, backgroundColor, backgroundRadius, textColor, textFont, toastDelay, inDelay, outDelay)

object Toast {
    fun show(
            ownerStage: Stage? = null,
            message: String,
            backgroundColor: Paint = Color.BLACK,
            backgroundRadius: Double = 10.0,
            textColor: Paint = Color.WHITE,
            textFont: Font = Font.font(20.0),
            toastDelay: Long = 1200,
            inDelay: Double = 500.0,
            outDelay: Double = 500.0
    ) {
        val stage = Stage().apply {
            ownerStage?.let { initOwner(it) }
            isResizable = false
            initStyle(StageStyle.TRANSPARENT)
        }

        val text = Text().apply {
            font = textFont
            fill = textColor
            text = message
        }

        val root = StackPane().apply {
            padding = Insets(
                    20.0,
                    50.0,
                    20.0,
                    50.0
            )
            background = Background(
                    BackgroundFill(
                            backgroundColor,
                            CornerRadii(backgroundRadius),
                            null
                    )
            )
            children.add(text)
        }

        val scene = Scene(root).apply {
            fill = Color.TRANSPARENT
        }

        stage.scene = scene
        stage.show()

        anim(
                inDelay,
                KeyValue(
                        root.opacityProperty(),
                        1
                )
        ) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    anim(
                            outDelay,
                            KeyValue(
                                    root.opacityProperty(),
                                    0
                            )
                    ){
                        stage.close()
                    }
                }
            }, toastDelay)
        }
    }


    private fun anim(duration: Double, vararg keyValues: KeyValue, finishListener: (() -> Unit)? = null) {
        Timeline().apply {
            keyFrames.add(KeyFrame(
                    Duration(duration),
                    *keyValues
            ))

            onFinished = EventHandler {
                finishListener?.invoke()
            }
        }.play()
    }
}