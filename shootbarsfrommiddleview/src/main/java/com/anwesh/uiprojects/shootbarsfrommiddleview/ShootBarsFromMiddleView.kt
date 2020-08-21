package com.anwesh.uiprojects.shootbarsfrommiddleview

/**
 * Created by anweshmishra on 22/08/20.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.RectF
import android.content.Context
import android.app.Activity

val colors : Array<String> = arrayOf("#3F51B5", "#F44336", "#03A9F4", "#4CAF50", "#FF5722")
val bars : Int = 5
val parts : Int = 2
val scGap : Float = 0.02f / (parts * bars)
val backColor : Int = Color.parseColor("#BDBDBD")
val delay : Long = 20


fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()
