package com.djacoronel.goalmentum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.EditMilestoneInteractor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository


/**
 * Created by djacoronel on 10/6/17.
 */

class EditMilestoneInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val milestoneRepository: MilestoneRepository,
        private val callback: EditMilestoneInteractor.Callback,
        private val updatedMilestone: Milestone
) : AbstractInteractor(threadExecutor, mainThread), EditMilestoneInteractor {

    override fun run() {
        val milestoneId = updatedMilestone.id
        val milestoneToEdit = milestoneRepository.getMilestoneById(milestoneId)

        if (milestoneToEdit == null) {
            milestoneRepository.insert(updatedMilestone)
        } else {
            milestoneRepository.update(updatedMilestone)
        }

        mMainThread.post(Runnable { callback.onMilestoneUpdated(updatedMilestone) })
    }
}