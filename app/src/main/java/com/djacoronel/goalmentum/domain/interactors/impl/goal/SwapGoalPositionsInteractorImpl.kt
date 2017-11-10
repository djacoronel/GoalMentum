package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.SwapGoalPositionsInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository

/**
 * Created by djacoronel on 11/11/17.
 */
class SwapGoalPositionsInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val goalRepository: GoalRepository,
        private val callback: SwapGoalPositionsInteractor.Callback,
        private val goal1: Goal,
        private val goal2: Goal
) : AbstractInteractor(threadExecutor, mainThread), SwapGoalPositionsInteractor {

    override fun run() {
        goalRepository.update(goal1)
        goalRepository.update(goal2)

        mMainThread.post(Runnable { callback.onGoalPositionsSwapped() })
    }
}