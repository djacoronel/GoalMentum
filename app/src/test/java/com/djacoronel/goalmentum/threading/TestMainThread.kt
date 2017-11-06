package com.djacoronel.goalmentum.threading

import com.djacoronel.goalmentum.domain.executor.MainThread

/**
 * Created by djacoronel on 11/6/17.
 */

class TestMainThread : MainThread {

    override fun post(runnable: Runnable) {
        // tests can run on this thread, no need to invoke other threads
        runnable.run()
    }
}