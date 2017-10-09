package com.djacoronel.goalmentum.domain.executor.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * This singleton class will make sure that each interactor operation gets a background thread.
 * </p>
 * Created by djacoronel on 10/6/17.
 */
class ThreadExecutor private constructor() : Executor {

    private val mThreadPoolExecutor: ThreadPoolExecutor

    init {
        val keepAlive = KEEP_ALIVE_TIME.toLong()
        mThreadPoolExecutor = ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                keepAlive,
                TIME_UNIT,
                WORK_QUEUE)
    }

    override fun execute(interactor: AbstractInteractor) {
        mThreadPoolExecutor.submit({
            // run the main logic
            interactor.run()

            // mark it as finished
            interactor.onFinished()
        })
    }

    companion object {

        // This is a singleton
        @Volatile private var sThreadExecutor: ThreadExecutor? = null

        private val CORE_POOL_SIZE = 3
        private val MAX_POOL_SIZE = 5
        private val KEEP_ALIVE_TIME = 120
        private val TIME_UNIT = TimeUnit.SECONDS
        private val WORK_QUEUE = LinkedBlockingQueue<Runnable>()

        /**
         * Returns a singleton instance of this executor. If the executor is not initialized then it initializes it and returns
         * the instance.
         */
        val instance: Executor
            get() {
                return if (sThreadExecutor == null) {
                    sThreadExecutor = ThreadExecutor()
                    sThreadExecutor as ThreadExecutor
                } else
                    sThreadExecutor as ThreadExecutor
            }
    }
}