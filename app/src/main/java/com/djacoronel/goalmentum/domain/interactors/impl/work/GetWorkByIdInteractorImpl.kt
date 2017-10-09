package com.djacoronel.workmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.GetWorkByIdInteractor
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class GetWorkByIdInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mWorkId: Long,
        private val mWorkRepository: WorkRepository,
        private val mCallback: GetWorkByIdInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetWorkByIdInteractor {

    override fun run() {
        val work = mWorkRepository.getWorkById(mWorkId)

        if (work == null) {
            mMainThread.post(Runnable { mCallback.noWorkFound() })
        } else {
            mMainThread.post(Runnable { mCallback.onWorkRetrieved(work) })
        }
    }
}