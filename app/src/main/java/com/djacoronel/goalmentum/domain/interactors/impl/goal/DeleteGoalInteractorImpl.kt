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
        private val mGoalRepository: GoalRepository,
        private val mCallback: DeleteGoalInteractor.Callback,
        private val mGoal: Goal
) : AbstractInteractor(threadExecutor, mainThread), DeleteGoalInteractor {

    override fun run() {
            mGoalRepository.delete(mGoal)
            mMainThread.post(Runnable { mCallback.onGoalDeleted(mGoal) })
    }
}