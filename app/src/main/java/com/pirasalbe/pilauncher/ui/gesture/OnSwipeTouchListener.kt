package com.pirasalbe.pilauncher.ui.gesture

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import com.pirasalbe.pilauncher.entity.LOG
import kotlin.math.abs

private const val TAG = LOG + "OnSwipeTouchListener"
private const val SWIPE_THRESHOLD = 100
private const val SWIPE_VELOCITY_THRESHOLD = 100

/**
 * Gesture listener
 * https://stackoverflow.com/a/43587450
 */
abstract class OnSwipeTouchListener : View.OnTouchListener {

    private val gestureDetector: GestureDetector

    constructor(context: Context) {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    /**
     * Translate a generic gesture to a specific one
     */
    private inner class GestureListener : SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                // get distance between events
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x

                // horizontal gesture fast and long enough
                if (abs(diffX) > abs(diffY) && abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    result = true
                } else if (abs(diffX) < abs(diffY) && abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    // vertical gesture fast and long enough
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                Log.e(TAG, "Swipe error", exception)
            }

            return result
        }

    }

    /**
     * Swipe Right
     */
    abstract fun onSwipeRight()

    /**
     * Swipe Left
     */
    abstract fun onSwipeLeft()

    /**
     * Swipe Top
     */
    abstract fun onSwipeTop()

    /**
     * Swipe Bottom
     */
    abstract fun onSwipeBottom()

}