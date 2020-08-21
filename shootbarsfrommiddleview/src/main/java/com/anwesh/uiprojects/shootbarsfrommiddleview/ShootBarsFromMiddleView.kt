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

fun Canvas.drawBarsFromMiddle(scale : Float, w : Float, h : Float, paint : Paint) {
    val gap : Float = w / bars
    val sf : Float = scale.sinify()
    val sf1 : Float = sf.divideScale(0, parts)
    val sf2 : Float = sf.divideScale(1, parts)
    for (j in 0..(bars - 1)) {
        val sf1j : Float = sf1.divideScale(j, bars)
        val sf2j : Float = sf2.divideScale(Math.abs(j - bars / 2), bars)
        save()
        translate(gap * j, h * (1 - sf2j))
        drawRect(RectF(-gap * sf1j, 0f, gap, gap * sf1j), paint)
        restore()
    }
}

fun Canvas.drawBFMNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = Color.parseColor(colors[i])
    drawBarsFromMiddle(scale, w, h, paint)
}

class ShootBarsFromMiddleView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}