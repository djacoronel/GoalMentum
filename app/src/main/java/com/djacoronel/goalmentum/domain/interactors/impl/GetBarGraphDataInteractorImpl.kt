package com.djacoronel.goalmentum.domain.interactors.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetBarGraphDataInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetLineGraphDataInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.util.DateUtils

/**
 * Created by djacoronel on 10/31/17.
 */
class GetBarGraphDataInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val goalRepository: GoalRepository,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository,
        private val callback: GetBarGraphDataInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetLineGraphDataInteractor {
    override fun run() {
        val goals = goalRepository.allGoals

        for (goal in goals) {
            val works = mutableListOf<Work>()
            val milestones = milestoneRepository.getMilestonesByAssignedGoal(goal.id)

            milestones.forEach { works.addAll(workRepository.getWorksByAssignedMilestone(it.id)) }

            val numberOfActiveWork = works.filter { it.achieved == false }.size
            val numberOfAchievedWork = works.filter { it.achieved == true }.size
            val numberOfAchievedWorkToday = works.filter { it.dateAchieved == DateUtils.today }.size
            val numberOfAchievedMilestone = milestones.filter { it.achieved == true }.size

            goal.activeWork = numberOfActiveWork
            goal.achievedWork = numberOfAchievedWork
            goal.achievedWorkToday = numberOfAchievedWorkToday
            goal.achievedMilestone = numberOfAchievedMilestone
        }

        val sortedGoalList = goals.sortedBy { it.achievedWork }
        val dataPairs = mutableListOf<Pair<String, Int>>()
        val index = sortedGoalList.lastIndex

        for (i in 0..6) {
            if (i <= index)
                dataPairs.add(Pair(sortedGoalList[i].description!!, sortedGoalList[i].achievedWork))
            else
                dataPairs.add(0, Pair("", 0))
        }

        mMainThread.post(Runnable { callback.onBarGraphDataRetrieved(dataPairs) })
    }
}