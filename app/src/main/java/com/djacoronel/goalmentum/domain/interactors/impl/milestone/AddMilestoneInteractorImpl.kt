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
        private val callback: AddMilestoneInteractor.Callback,
        private val milestoneRepository: MilestoneRepository,
        private val assignedGoal: Long,
        private val description: String
) : AbstractInteractor(threadExecutor, mainThread), AddMilestoneInteractor {

    override fun run() {
        val milestone = Milestone(assignedGoal, description)
        milestoneRepository.insert(milestone)
        mMainThread.post(Runnable { callback.onMilestoneAdded(milestone) })
    }
}