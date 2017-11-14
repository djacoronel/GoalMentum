package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.AddWorkInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class AddWorkInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val callback: AddWorkInteractor.Callback,
        private val workRepository: WorkRepository,
        private val assignedMilestone: Long,
        private val description: String
) : AbstractInteractor(threadExecutor, mainThread), AddWorkInteractor {

    override fun run() {
        val newWorkPosition = workRepository.allWorks.lastIndex + 1
        val work = Work(newWorkPosition, assignedMilestone, description)
        workRepository.insert(work)
        mMainThread.post(Runnable { callback.onWorkAdded(work) })
    }
}