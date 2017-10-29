package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdAndSetAchievedInteractor
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class GetGoalByIdAndSetAchievedInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val callback: GetGoalByIdAndSetAchievedInteractor.Callback,
        private val goalRepository: GoalRepository,
        private val milestoneRepository: MilestoneRepository,
        private val goalId: Long
) : AbstractInteractor(threadExecutor, mainThread), EditGoalInteractor {

    override fun run() {
        val mMilestones = milestoneRepository.getMilestonesByAssignedGoal(goalId)

        val milestonesAchieved = mMilestones.filter { it.achieved == true }
        val isAllMilestoneAchieved = milestonesAchieved.size == mMilestones.size && mMilestones.isNotEmpty()

        if (isAllMilestoneAchieved) {
            val goalToAchieve = goalRepository.getGoalById(goalId)

            goalToAchieve?.let {
                it.achieved = true
                goalRepository.update(goalToAchieve)
                mMainThread.post(Runnable { callback.onGoalAchieved(it) })
            }
        }
    }
}