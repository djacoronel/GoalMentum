package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalProgressInteractor
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 10/22/17.
 */
class GetGoalProgressInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mGoalRepository: GoalRepository,
        private val mMilestoneRepository: MilestoneRepository,
        private val mWorkRepository: WorkRepository,
        private val mCallback: GetGoalProgressInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetGoalProgressInteractor {

    override fun run() {
        val progress = hashMapOf<Goal, Array<Int>>()
        val goals = mGoalRepository.allGoals

        for (goal in goals){
            val works = mutableListOf<Work>()
            val milestones = mMilestoneRepository.getMilestonesByAssignedGoal(goal.id)

            for (milestone in milestones){
                works.addAll(mWorkRepository.getWorksByAssignedMilestone(milestone.id))
            }

            val numberOfActiveWork = works.filter { it.achieved == false }.size
            val numberOfAchievedWork = works.filter { it.achieved == true }.size

            progress.put(goal, arrayOf(numberOfActiveWork,numberOfAchievedWork))
        }

        mMainThread.post(Runnable { mCallback.onGoalProgressRetrieved(progress)})
    }
}