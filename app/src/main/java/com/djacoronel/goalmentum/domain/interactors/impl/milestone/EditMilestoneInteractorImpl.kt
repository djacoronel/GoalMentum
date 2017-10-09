package com.djacoronel.goalmentum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.EditMilestoneInteractor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import java.util.*


/**
 * Created by djacoronel on 10/6/17.
 */

class EditMilestoneInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mCallback: EditMilestoneInteractor.Callback,
        private val mMilestoneRepository: MilestoneRepository,
        private val mUpdatedMilestone: Milestone,
        private val mAssignedGoal: Long,
        private val mDescription: String,
        private val mDate: Date
) : AbstractInteractor(threadExecutor, mainThread), EditMilestoneInteractor {

    override fun run() {
        val milestoneId = mUpdatedMilestone.id
        var milestoneToEdit = mMilestoneRepository.getMilestoneById(milestoneId)

        if (milestoneToEdit == null) {
            milestoneToEdit = Milestone(mAssignedGoal, mDescription)
            mMilestoneRepository.insert(milestoneToEdit)
        } else {
            milestoneToEdit.assignedGoal = mAssignedGoal
            milestoneToEdit.description = mDescription
            milestoneToEdit.date = mDate
            mMilestoneRepository.update(milestoneToEdit)
        }

        mMainThread.post(Runnable { mCallback.onMilestoneUpdated(mUpdatedMilestone) })
    }
}