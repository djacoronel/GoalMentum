package com.djacoronel.goalmentum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.AddMilestoneInteractor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class AddMilestoneInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mCallback: AddMilestoneInteractor.Callback,
        private val mMilestoneRepository: MilestoneRepository,
        private val mAssignedGoal: Long,
        private val mDescription: String
) : AbstractInteractor(threadExecutor, mainThread), AddMilestoneInteractor {

    override fun run() {
        val milestone = Milestone(mAssignedGoal, mDescription)
        mMilestoneRepository.insert(milestone)

        mMainThread.post(Runnable { mCallback.onMilestoneAdded(milestone) })
    }
}