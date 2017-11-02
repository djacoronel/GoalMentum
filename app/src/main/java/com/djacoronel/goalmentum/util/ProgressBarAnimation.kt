package com.djacoronel.goalmentum.util

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

class ProgressBarAnimation
(private val mProgressBar: ProgressBar, fullDuration: Long) : Animation() {
    private var mTo: Int = 0
    private var mFrom: Int = 0
    private val mStepDuration: Long = fullDuration / mProgressBar.max

    fun setProgress(progress: Int) {
        var progressCopy = progress
        if (progressCopy < 0)
            progressCopy = 0

        if (progressCopy > mProgressBar.max)
            progressCopy = mProgressBar.max

        mTo = progressCopy
        mFrom = mProgressBar.progress
        duration = Math.abs(mTo - mFrom) * mStepDuration
        mProgressBar.startAnimation(this)
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val value = mFrom + (mTo - mFrom) * interpolatedTime
        mProgressBar.progress = value.toInt()
    }
}