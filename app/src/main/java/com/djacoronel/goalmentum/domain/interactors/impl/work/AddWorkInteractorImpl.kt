package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.AddWorkInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */

class AddWorkInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mCallback: AddWorkInteractor.Callback,
        private val mWorkRepository: WorkRepository,
        private val mAssignedWork: Long,
        private val mDescription: String
) : AbstractInteractor(threadExecutor, mainThread), AddWorkInteractor {

    override fun run() {
        val work = Work(mAssignedWork, mDescription)
        mWorkRepository.insert(work)

        mMainThread.post(Runnable { mCallback.onWorkAdded() })
    }
}