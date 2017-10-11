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
        private val mUpdatedWork: Work
) : AbstractInteractor(threadExecutor, mainThread), EditWorkInteractor {

    override fun run() {
        val workId = mUpdatedWork.id
        val workToEdit = mWorkRepository.getWorkById(workId)

        if (workToEdit == null) {
            mWorkRepository.insert(mUpdatedWork)
        } else {
            mWorkRepository.update(mUpdatedWork)
        }

        mMainThread.post(Runnable { mCallback.onWorkUpdated(mUpdatedWork) })
    }
}