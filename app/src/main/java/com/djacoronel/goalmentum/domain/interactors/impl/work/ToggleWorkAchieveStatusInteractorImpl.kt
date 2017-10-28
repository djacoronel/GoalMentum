package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.EditWorkInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.ToggleWorkAchieveStatusInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.util.DateUtils

/**
 * Created by djacoronel on 10/6/17.
 */

class ToggleWorkAchieveStatusInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val callback: ToggleWorkAchieveStatusInteractor.Callback,
        private val workRepository: WorkRepository,
        private val work: Work
) : AbstractInteractor(threadExecutor, mainThread), EditWorkInteractor {

    override fun run() {
        val workToEdit = workRepository.getWorkById(work.id)

        workToEdit?.let {
            workToEdit.achieved = !workToEdit.achieved
            if (workToEdit.achieved == true)
                workToEdit.dateAchieved = DateUtils.today
            workRepository.update(workToEdit)
            mMainThread.post(Runnable { callback.onWorkAchieveStatusToggled(workToEdit) })
        }
    }
}