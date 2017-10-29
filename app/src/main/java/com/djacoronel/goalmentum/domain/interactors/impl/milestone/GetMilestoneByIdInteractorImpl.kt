package com.djacoronel.milestonementum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetMilestoneByIdInteractor
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class GetMilestoneByIdInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository,
        private val callback: GetMilestoneByIdInteractor.Callback,
        private val milestoneId: Long
) : AbstractInteractor(threadExecutor, mainThread), GetMilestoneByIdInteractor {

    override fun run() {
        val milestone = milestoneRepository.getMilestoneById(milestoneId)

        if (milestone == null) {
            mMainThread.post(Runnable { callback.noMilestoneFound() })
        } else {
            val works = workRepository.getWorksByAssignedMilestone(milestone.id)
            val worksAchieved = works.filter { it.achieved == true }
            val isAllWorkAchieved = worksAchieved.size == works.size && works.isNotEmpty()

            if (isAllWorkAchieved && milestone.achieved == false) {
                milestone.achieved = true
                milestoneRepository.update(milestone)
            } else if (!isAllWorkAchieved && milestone.achieved == true) {
                milestone.achieved = false
                milestoneRepository.update(milestone)
            }

            mMainThread.post(Runnable { callback.onMilestoneRetrieved(milestone) })

        }
    }
}