package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.DeleteWorkInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class DeleteWorkInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mWorkRepository: WorkRepository,
        private val mCallback: DeleteWorkInteractor.Callback,
        private val workId: Long
) : AbstractInteractor(threadExecutor, mainThread), DeleteWorkInteractor {

    override fun run() {
        val work = mWorkRepository.getWorkById(workId)

        work?.let {
            mWorkRepository.delete(it)
            mMainThread.post(Runnable { mCallback.onWorkDeleted(it.id) })
        }
    }
}