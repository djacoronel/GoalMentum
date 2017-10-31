package com.djacoronel.goalmentum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetAllMilestonesByAssignedGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetAllMilestonesInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class GetAllMilestonesByAssignedGoalInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository,
        private val mCallback: GetAllMilestonesByAssignedGoalInteractor.Callback,
        private val mGoalId: Long
) : AbstractInteractor(threadExecutor, mainThread), GetAllMilestonesInteractor {
    override fun run() {
        val milestones = milestoneRepository.getMilestonesByAssignedGoal(mGoalId)
        val worksPerMilestone = hashMapOf<Long, List<Work>>()

        for (milestone in milestones) {
            val works = workRepository.getWorksByAssignedMilestone(milestone.id)
            val worksAchieved = works.filter { it.achieved == true }
            val isAllWorkAchieved = worksAchieved.size == works.size && works.isNotEmpty()

            milestone.achievedWorks = worksAchieved.size
            milestone.totalWorks = works.size

            if (isAllWorkAchieved && milestone.achieved == false) {
                milestone.achieved = true
                milestoneRepository.update(milestone)
            } else if (!isAllWorkAchieved && milestone.achieved == true) {
                milestone.achieved = false
                milestoneRepository.update(milestone)
            }

            val activeWorks = works.filter { it.achieved == false }
            val displayedWorks = if (activeWorks.size < 3) activeWorks else activeWorks.subList(0, 3)

            worksPerMilestone.put(milestone.id, displayedWorks)
        }

        mMainThread.post(Runnable { mCallback.onMilestonesRetrieved(milestones, worksPerMilestone) })
    }
}