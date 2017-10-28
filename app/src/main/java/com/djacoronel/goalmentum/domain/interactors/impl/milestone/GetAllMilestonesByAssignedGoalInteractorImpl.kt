package com.djacoronel.goalmentum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetAllMilestonesByAssignedGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetAllMilestonesInteractor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */

class GetAllMilestonesByAssignedGoalInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mGoalId: Long,
        private val mMilestoneRepository: MilestoneRepository,
        private val mWorkRepository: WorkRepository,
        private val mCallback: GetAllMilestonesByAssignedGoalInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetAllMilestonesInteractor {
    override fun run() {
        val milestones = mMilestoneRepository.getMilestonesByAssignedGoal(mGoalId)
        val displayedWorks = hashMapOf<Long, List<Work>>()

        for (milestone in milestones){
            val works = mWorkRepository.getWorksByAssignedMilestone(milestone.id)
            displayedWorks.put(milestone.id, works)
        }

        mMainThread.post(Runnable { mCallback.onMilestonesRetrieved(milestones, displayedWorks) })
    }
}