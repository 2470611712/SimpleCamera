package com.example.YHcamera.CustomView

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.*
import com.example.YHcamera.R
import kotlinx.android.synthetic.main.activity_main.view.*

//实现了双指缩放功能的TextureView
class ScaleTextureView(context: Context, attributeSet: AttributeSet):TextureView(context,attributeSet) {
    private val TAG = "ZSRImageView"
    var scaleGestureDetector: ScaleGestureDetector
    //前一时刻和当前时刻的变换矩阵
    private var mImageMatrix: Matrix = Matrix()
    private val savedMatrix: Matrix = Matrix()
    //缩放中心
    private var x = 0
    private var y = 0
    //放缩倍数
    private var totalscale:Float=1f

    private var mLastAngle = 0

    // 第一个按下的手指的点
    private val startPoint = PointF()


    //缩放Listener
    private val mScaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            //更新缩放参数
            val scaleFactor = detector.scaleFactor
            totalscale*=scaleFactor
            //不允许缩小，产生空白
            if(totalscale<1){return true}

            mImageMatrix.postScale(scaleFactor, scaleFactor, x.toFloat(), y.toFloat())
            findViewById<ScaleTextureView>(R.id.texture_view)
            texture_view.setTransform(mImageMatrix)


            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return super.onScaleBegin(detector)
        }
    }

    init {
        scaleGestureDetector = ScaleGestureDetector(context, mScaleListener)
    }

    //计算中心
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            x = w / 2
            y = h / 2
        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            Log.d("drag", "onTouch: x= ${event.rawX.toInt()},y=${event.rawY.toInt()}" )
            getTransform(mImageMatrix)
            savedMatrix.set(mImageMatrix)
            startPoint[event.x] = event.y
            return true
        }
        return when (event.pointerCount) {
            2 -> scaleGestureDetector.onTouchEvent(event)
            else -> super.onTouchEvent(event)
        }
    }
}