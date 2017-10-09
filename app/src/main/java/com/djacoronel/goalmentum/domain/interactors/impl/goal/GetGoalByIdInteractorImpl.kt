package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdInteractor
import com.djacoronel.goalmentum.domain.repository.GoalRepository


/**
 * Created by djacoronel on 10/6/17.
 */

class GetGoalByIdInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mGoalId: Long,
        private val mGoalRepository: GoalRepository,
        private val mCallback: GetGoalByIdInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetGoalByIdInteractor {

    override fun run() {
        val goal = mGoalRepository.getGoalById(mGoalId)

        if (goal == null) {
            mMainThread.post(Runnable { mCallback.noGoalFound() })
        } else {
            mMainThread.post(Runnable { mCallback.onGoalRetrieved(goal) })
        }
    }
}