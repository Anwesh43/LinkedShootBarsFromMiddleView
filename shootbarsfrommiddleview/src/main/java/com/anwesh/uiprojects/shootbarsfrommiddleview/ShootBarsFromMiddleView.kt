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
        val sf2j : Float = sf2.divideScale(Math.abs(j - bars / 2), bars / 2 + 1)
        save()
        translate(gap * j, h * (1 - sf2j))
        drawRect(RectF(0f,-gap * sf1j, gap, 0f), paint)
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

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class SBFMNode(var i : Int, val state : State = State()) {

        private var next : SBFMNode? = null
        private var prev : SBFMNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = SBFMNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawBFMNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : SBFMNode {
            var curr : SBFMNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class ShootBarsFromMiddle(var i : Int) {

        private var curr : SBFMNode = SBFMNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : ShootBarsFromMiddleView) {

        private val animator : Animator = Animator(view)
        private val sbfm : ShootBarsFromMiddle = ShootBarsFromMiddle(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            sbfm.draw(canvas, paint)
            animator.animate {
                sbfm.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            sbfm.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity: Activity) : ShootBarsFromMiddleView {
            val view : ShootBarsFromMiddleView = ShootBarsFromMiddleView(activity)
            activity.setContentView(view)
            return view
        }
    }
}