package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.SwapGoalPositionsInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.SwapWorkPositionsInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.WorkRepository

/**
 * Created by djacoronel on 11/11/17.
 */
class SwapWorkPositionsInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val workRepository: WorkRepository,
        private val callback: SwapWorkPositionsInteractor.Callback,
        private val work1: Work,
        private val work2: Work
) : AbstractInteractor(threadExecutor, mainThread), SwapGoalPositionsInteractor {

    override fun run() {
        workRepository.update(work1)
        workRepository.update(work2)

        mMainThread.post(Runnable { callback.onWorkPositionsSwapped() })
    }
}