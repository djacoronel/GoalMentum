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
        private val callback: AddGoalInteractor.Callback,
        private val goalRepository: GoalRepository,
        private val description: String,
        private val duration: String
) : AbstractInteractor(threadExecutor, mainThread), AddGoalInteractor {

    override fun run() {
        val positionInList = goalRepository.allGoals.size

        val goal = Goal(positionInList, description, duration)

        goalRepository.insert(goal)

        mMainThread.post(Runnable { callback.onGoalAdded(goal.id) })
    }
}