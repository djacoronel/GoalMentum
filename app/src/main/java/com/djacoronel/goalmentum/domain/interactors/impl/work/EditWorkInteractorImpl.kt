package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.EditWorkInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */

class EditWorkInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mCallback: EditWorkInteractor.Callback,
        private val mWorkRepository: WorkRepository,
        private val mUpdatedWork: Work,
        private val mAssignedMilestone: Long,
        private val mDescription: String,
        private val mDate: Date
) : AbstractInteractor(threadExecutor, mainThread), EditWorkInteractor {

    override fun run() {
        val workId = mUpdatedWork.id
        var workToEdit = mWorkRepository.getWorkById(workId)

        if (workToEdit == null) {
            workToEdit = Work(mAssignedMilestone, mDescription)
            mWorkRepository.insert(workToEdit)
        } else {
            workToEdit.assignedMilestone = mAssignedMilestone
            workToEdit.description = mDescription
            workToEdit.date = mDate
            mWorkRepository.update(workToEdit)
        }

        mMainThread.post(Runnable { mCallback.onWorkUpdated(mUpdatedWork) })
    }
}