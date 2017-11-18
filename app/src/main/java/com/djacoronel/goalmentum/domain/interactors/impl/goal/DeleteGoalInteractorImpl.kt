package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.DeleteGoalInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository


/**
 * Created by djacoronel on 10/6/17.
 */

class DeleteGoalInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val goalRepository: GoalRepository,
        private val callback: DeleteGoalInteractor.Callback,
        private val goal: Goal
) : AbstractInteractor(threadExecutor, mainThread), DeleteGoalInteractor {

    override fun run() {
            goalRepository.delete(goal)
            mMainThread.post(Runnable { callback.onGoalDeleted(goal) })
    }
}