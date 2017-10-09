package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.DeleteGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetAllGoalsInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.goal.DeleteGoalInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetAllGoalsInteractorImpl
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter


/**
 * Created by djacoronel on 10/7/17.
 */
class GoalPresenterImpl(
        executor: Executor,
        mainThread: MainThread,
        private val mView: GoalPresenter.View,
        private val mGoalRepository: GoalRepository
) : AbstractPresenter(executor, mainThread), GoalPresenter, GetAllGoalsInteractor.Callback, DeleteGoalInteractor.Callback {

    override fun resume() {
        getAllGoals()
    }

    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {

    }

    override fun onError(message: String) {

    }

    override fun getAllGoals() {
        val getGoalsInteractor = GetAllGoalsInteractorImpl(
                mExecutor,
                mMainThread,
                mGoalRepository,
                this
        )
        getGoalsInteractor.execute()
    }

    override fun onGoalsRetrieved(goalList: List<Goal>) {
        mView.showGoals(goalList)
    }

    override fun deleteGoal(goalId: Long) {
        val deleteCostInteractor = DeleteGoalInteractorImpl(
                mExecutor,
                mMainThread,
                goalId,
                this,
                mGoalRepository
        )
        deleteCostInteractor.execute()
    }

    override fun onGoalDeleted(goal: Goal) {
        mView.onGoalDeleted(goal)
    }
}