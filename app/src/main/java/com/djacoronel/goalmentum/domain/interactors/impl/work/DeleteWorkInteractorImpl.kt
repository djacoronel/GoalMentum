package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.DeleteWorkInteractor
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class DeleteWorkInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mWorkId: Long,
        private val mCallback: DeleteWorkInteractor.Callback,
        private val mWorkRepository: WorkRepository
) : AbstractInteractor(threadExecutor, mainThread), DeleteWorkInteractor {

    override fun run() {

        val work = mWorkRepository.getWorkById(mWorkId)

        if (work != null) {
            mWorkRepository.delete(work)
            mMainThread.post(Runnable { mCallback.onWorkDeleted(work) })
        }
    }
}