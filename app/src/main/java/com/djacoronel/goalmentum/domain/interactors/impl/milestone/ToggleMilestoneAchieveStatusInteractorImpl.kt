package com.djacoronel.goalmentum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.EditMilestoneInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.ToggleMilestoneAchieveStatusInteractor
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 10/6/17.
 */

class ToggleMilestoneAchieveStatusInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val callback: ToggleMilestoneAchieveStatusInteractor.Callback,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository,
        private val milestoneId: Long
) : AbstractInteractor(threadExecutor, mainThread), EditMilestoneInteractor {

    override fun run() {
        val milestoneToEdit = milestoneRepository.getMilestoneById(milestoneId)

        milestoneToEdit?.let {
            val works = workRepository.getWorksByAssignedMilestone(milestoneToEdit.id)
            val worksAchieved = works.filter { it.achieved == true }
            val isAllWorkAchieved = worksAchieved.size == works.size


            if (isAllWorkAchieved && milestoneToEdit.achieved == false) {
                milestoneToEdit.achieved = true
                milestoneRepository.update(milestoneToEdit)
            } else if (!isAllWorkAchieved && milestoneToEdit.achieved == true) {
                milestoneToEdit.achieved = false
                milestoneRepository.update(milestoneToEdit)

            }

            mMainThread.post(Runnable { callback.onMilestoneAchieveStatusUpdated(milestoneToEdit) })
        }
    }
}