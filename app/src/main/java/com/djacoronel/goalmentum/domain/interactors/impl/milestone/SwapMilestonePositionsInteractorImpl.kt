package com.djacoronel.goalmentum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.SwapGoalPositionsInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.SwapMilestonePositionsInteractor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository

/**
 * Created by djacoronel on 11/11/17.
 */
class SwapMilestonePositionsInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val milestoneRepository: MilestoneRepository,
        private val callback: SwapMilestonePositionsInteractor.Callback,
        private val milestone1: Milestone,
        private val milestone2: Milestone
) : AbstractInteractor(threadExecutor, mainThread), SwapGoalPositionsInteractor {

    override fun run() {
        milestoneRepository.update(milestone1)
        milestoneRepository.update(milestone2)

        mMainThread.post(Runnable { callback.onMilestonePositionsSwapped() })
    }
}