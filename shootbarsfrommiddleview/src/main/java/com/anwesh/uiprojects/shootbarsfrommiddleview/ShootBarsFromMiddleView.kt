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
