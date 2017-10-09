package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.AddGoalInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository


class AddGoalInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mCallback: AddGoalInteractor.Callback,
        private val mGoalRepository: GoalRepository,
        private val mDescription: String,
        private val mDuration: String
) : AbstractInteractor(threadExecutor, mainThread), AddGoalInteractor {

    override fun run() {
        // create a new cost object and insert it
        val goal = Goal(mDescription, mDuration)
        mGoalRepository.insert(goal)

        // notify on the main thread that we have inserted this item
        mMainThread.post(Runnable { mCallback.onGoalAdded() })
    }
}