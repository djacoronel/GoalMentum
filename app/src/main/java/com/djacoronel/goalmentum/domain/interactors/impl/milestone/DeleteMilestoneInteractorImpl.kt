package com.djacoronel.goalmentum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.DeleteMilestoneInteractor
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class DeleteMilestoneInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mMilestoneRepository: MilestoneRepository,
        private val mCallback: DeleteMilestoneInteractor.Callback,
        private val mMilestoneId: Long
) : AbstractInteractor(threadExecutor, mainThread), DeleteMilestoneInteractor {

    override fun run() {

        val milestone = mMilestoneRepository.getMilestoneById(mMilestoneId)

        milestone?.let {
            mMilestoneRepository.delete(it)
            mMainThread.post(Runnable { mCallback.onMilestoneDeleted(it.id) })
        }
    }
}