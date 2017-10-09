package com.djacoronel.milestonementum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetMilestoneByIdInteractor
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class GetMilestoneByIdInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mMilestoneId: Long,
        private val mMilestoneRepository: MilestoneRepository,
        private val mCallback: GetMilestoneByIdInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetMilestoneByIdInteractor {

    override fun run() {
        val milestone = mMilestoneRepository.getMilestoneById(mMilestoneId)

        if (milestone == null) {
            mMainThread.post(Runnable { mCallback.noMilestoneFound() })
        } else {
            mMainThread.post(Runnable { mCallback.onMilestoneRetrieved(milestone) })
        }
    }
}