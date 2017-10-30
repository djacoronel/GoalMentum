package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.EditWorkInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.ToggleWorkAchieveStatusInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.util.DateUtils

/**
 * Created by djacoronel on 10/6/17.
 */

class ToggleWorkAchieveStatusInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val callback: ToggleWorkAchieveStatusInteractor.Callback,
        private val goalRepository: GoalRepository,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository,
        private val work: Work
) : AbstractInteractor(threadExecutor, mainThread), EditWorkInteractor {

    override fun run() {
        updateWorkAchieveStatus(work.id)
    }

    fun updateWorkAchieveStatus(workId: Long) {
        val workToEdit = workRepository.getWorkById(workId)

        workToEdit?.let {
            it.achieved = !it.achieved
            if (it.achieved == true)
                it.dateAchieved = DateUtils.today
            workRepository.update(it)

            updateMilestoneAchieveStatus(it.assignedMilestone)

            mMainThread.post(Runnable { callback.onWorkAchieveStatusToggled(it) })
        }
    }

    fun updateMilestoneAchieveStatus(milestoneId: Long) {
        val milestoneToEdit = milestoneRepository.getMilestoneById(milestoneId)

        milestoneToEdit?.let {
            val works = workRepository.getWorksByAssignedMilestone(it.id)
            val worksAchieved = works.filter { it.achieved == true }
            val isAllWorkAchieved = worksAchieved.size == works.size && works.isNotEmpty()

            if (isAllWorkAchieved && it.achieved == false) {
                it.achieved = true
                milestoneRepository.update(it)
            } else if (!isAllWorkAchieved && it.achieved == true) {
                it.achieved = false
                milestoneRepository.update(it)
            }

            updateGoalMomentum(it.assignedGoal)
            updateGoalAchieveStatus(it.assignedGoal)
        }
    }

    fun updateGoalMomentum(goalId: Long) {
        val goalToUpdate = goalRepository.getGoalById(goalId)

        goalToUpdate?.let {
            val momentum = if (work.achieved == true) -10 else 10
            it.applyDailyMomentumDeductions()
            it.updateMomentum(momentum)
            it.momentumDateUpdated = DateUtils.today

            goalRepository.update(it)
        }
    }

    fun updateGoalAchieveStatus(goalId: Long) {
        val goalToAchieve = goalRepository.getGoalById(goalId)

        goalToAchieve?.let {
            val mMilestones = milestoneRepository.getMilestonesByAssignedGoal(goalId)

            val milestonesAchieved = mMilestones.filter { it.achieved == true }
            val isAllMilestoneAchieved = milestonesAchieved.size == mMilestones.size && mMilestones.isNotEmpty()

            if (isAllMilestoneAchieved) {
                it.achieved = true
                goalRepository.update(goalToAchieve)
            } else {
                it.achieved = false
                goalRepository.update(goalToAchieve)
            }
        }
    }
}