package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.util.DateUtils


/**
 * Created by djacoronel on 10/6/17.
 */

class GetGoalByIdInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val goalRepository: GoalRepository,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository,
        private val callback: GetGoalByIdInteractor.Callback,
        private val goalId: Long
) : AbstractInteractor(threadExecutor, mainThread), GetGoalByIdInteractor {

    override fun run() {
        val goal = goalRepository.getGoalById(goalId)

        goal?.let {
            val works = mutableListOf<Work>()
            val milestones = milestoneRepository.getMilestonesByAssignedGoal(it.id)

            for (milestone in milestones) {
                works.addAll(workRepository.getWorksByAssignedMilestone(milestone.id))
            }

            val numberOfActiveWork = works.filter { it.achieved == false }.size
            val numberOfAchievedWork = works.filter { it.achieved == true }.size
            val numberOfAchievedWorkToday = works.filter { it.achieved == true && it.dateAchieved == DateUtils.today }.size
            val numberOfAchievedMilestone = milestones.filter { it.achieved == true }.size

            it.activeWork = numberOfActiveWork
            it.achievedWork = numberOfAchievedWork
            it.achievedWorkToday = numberOfAchievedWorkToday
            it.achievedMilestone = numberOfAchievedMilestone

            mMainThread.post(Runnable { callback.onGoalRetrieved(it) })
        }
    }
}