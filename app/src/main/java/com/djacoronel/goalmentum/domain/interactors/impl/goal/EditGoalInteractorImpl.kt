package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository


/**
 * Created by djacoronel on 10/6/17.
 */

class EditGoalInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mGoalRepository: GoalRepository,
        private val mCallback: EditGoalInteractor.Callback,
        private val mUpdatedGoal: Goal
) : AbstractInteractor(threadExecutor, mainThread), EditGoalInteractor {

    override fun run() {
        val goalId = mUpdatedGoal.id
        var goalToEdit = mGoalRepository.getGoalById(goalId)

        if (goalToEdit == null) {
            goalToEdit = mUpdatedGoal
            mGoalRepository.insert(goalToEdit)
        } else {
            goalToEdit = mUpdatedGoal
            mGoalRepository.update(goalToEdit)
        }

        mMainThread.post(Runnable { mCallback.onGoalUpdated(mUpdatedGoal) })
    }
}