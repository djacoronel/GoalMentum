package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import java.util.*


/**
 * Created by djacoronel on 10/6/17.
 */

class EditGoalInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mCallback: EditGoalInteractor.Callback,
        private val mGoalRepository: GoalRepository,
        private val mUpdatedGoal: Goal,
        private val mDescription: String,
        private val mDate: Date,
        private val mDuration: String
) : AbstractInteractor(threadExecutor, mainThread), EditGoalInteractor {

    override fun run() {
        val goalId = mUpdatedGoal.id
        var goalToEdit = mGoalRepository.getGoalById(goalId)

        if (goalToEdit == null) {
            goalToEdit = Goal(mDescription, mDuration)
            mGoalRepository.insert(goalToEdit)
        } else {
            goalToEdit.description = mDescription
            goalToEdit.date = mDate
            goalToEdit.duration = mDuration

            mGoalRepository.update(goalToEdit)
        }

        mMainThread.post(Runnable { mCallback.onGoalUpdated(mUpdatedGoal) })
    }
}