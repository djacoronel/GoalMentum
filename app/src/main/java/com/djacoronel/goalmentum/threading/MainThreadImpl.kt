package com.djacoronel.goalmentum.threading

import android.os.Handler
import android.os.Looper
import com.djacoronel.goalmentum.domain.executor.MainThread


/**
 * Created by djacoronel on 10/7/17.
 */
class MainThreadImpl private constructor() : MainThread {

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    override fun post(runnable: Runnable) {
        mHandler.post(runnable)
    }

    companion object {

        private var sMainThread: MainThread? = null

        val instance: MainThread
            get() {
                if (sMainThread == null) {
                    sMainThread = MainThreadImpl()
                }

                return sMainThread as MainThread
            }
    }
}