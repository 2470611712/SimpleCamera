package com.example.YHcamera.CustomView

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.example.YHcamera.R
import kotlin.math.atan

//实现了单指移动、双指缩放、三支旋转的ImageView
class ZSRImageView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private val TAG = "ZSRImageView"
    private var scaleGestureDetector: ScaleGestureDetector

    private var mImageMatrix: Matrix = Matrix()
    private val savedMatrix: Matrix = Matrix()

    private var x = 0
    private var y = 0

    private var mLastAngle = 0

    // 第一个按下的手指的点
    private val startPoint = PointF()


    //双指缩放
    private val mScaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {

            val scaleFactor = detector.scaleFactor
            mImageMatrix.postScale(scaleFactor, scaleFactor, x.toFloat(), y.toFloat())

            findViewById<ImageView>(R.id.photoView).imageMatrix = mImageMatrix



            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return super.onScaleBegin(detector)
        }
    }

    init {
        scaleGestureDetector = ScaleGestureDetector(context, mScaleListener)
        scaleType = ScaleType.MATRIX
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            val transX = (w - drawable.intrinsicWidth) / 2.0
            val transY = (h - drawable.intrinsicHeight) / 2.0
            mImageMatrix.setTranslate(transX.toFloat(), transY.toFloat())
            imageMatrix = mImageMatrix
            x = w / 2
            y = h / 2
        }
    }


    //实现三指旋转
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun doRotationEvent(ev: MotionEvent): Boolean {
        //计算两个手指的角度
        val dx = ev.getRawX(0) - ev.getRawY(1)
        val dy = ev.getRawX(0) - ev.getRawY(1)
        //弧度
        val radians = atan(dy.toDouble() / dx.toDouble())
        //角度
        val degrees = (radians * 180 / Math.PI).toInt()

        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> mLastAngle =
                degrees
            MotionEvent.ACTION_MOVE -> {
                when {
                    (degrees - mLastAngle) > 45 -> mImageMatrix.postRotate(
                        -5f,
                        x.toFloat(),
                        y.toFloat()
                    )
                    (degrees - mLastAngle) < -45 -> mImageMatrix.postRotate(
                        5f,
                        x.toFloat(),
                        y.toFloat()
                    )
                    else -> mImageMatrix.postRotate(
                        (degrees - mLastAngle).toFloat(),
                        x.toFloat(),
                        y.toFloat()
                    )
                }
                imageMatrix = mImageMatrix
                mLastAngle = degrees
            }
        }
        return true
    }

    //实现单指移动
    private fun doMoveEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                mImageMatrix.set(savedMatrix)
                mImageMatrix.postTranslate(ev.rawX - startPoint.x, ev.rawY - startPoint.y)
                imageMatrix = mImageMatrix
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            mImageMatrix.set(imageMatrix)
            savedMatrix.set(mImageMatrix)
            startPoint[event.rawX] = event.rawY
            return true
        }
        return when (event.pointerCount) {
//            3 -> doRotationEvent(event)
            2 -> scaleGestureDetector.onTouchEvent(event)
            1 -> doMoveEvent(event)
            else -> super.onTouchEvent(event)
        }
    }
}