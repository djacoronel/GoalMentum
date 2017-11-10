package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.DeleteGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetAllGoalsInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.SwapGoalPositionsInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.goal.DeleteGoalInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.EditGoalInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetAllGoalsInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.SwapGoalPositionsInteractorImpl
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.MainPresenter


/**
 * Created by djacoronel on 10/7/17.
 */
class MainPresenterImpl(
        executor: Executor,
        mainThread: MainThread,
        private val mView: MainPresenter.View,
        private val mGoalRepository: GoalRepository,
        private val mMilestoneRepository: MilestoneRepository,
        private val mWorkRepository: WorkRepository
) : AbstractPresenter(executor, mainThread), MainPresenter,
        GetAllGoalsInteractor.Callback,
        EditGoalInteractor.Callback,
        DeleteGoalInteractor.Callback,
        SwapGoalPositionsInteractor.Callback
{

    override fun getAllGoals() {
        val getGoalsInteractor = GetAllGoalsInteractorImpl(
                mExecutor,
                mMainThread,
                mGoalRepository,
                mMilestoneRepository,
                mWorkRepository,
                this
        )
        getGoalsInteractor.execute()
    }

    override fun onGoalsRetrieved(goalList: List<Goal>) {
        mView.showGoals(goalList)
    }

    override fun updateGoal(goal: Goal) {
        val editGoalInteractor = EditGoalInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mGoalRepository,
                goal
        )
        editGoalInteractor.execute()
    }

    override fun onGoalUpdated(goal: Goal) {
        mView.onGoalUpdated(goal)
    }

    override fun deleteGoal(goal: Goal) {
        val deleteCostInteractor = DeleteGoalInteractorImpl(
                mExecutor,
                mMainThread,
                goal,
                this,
                mGoalRepository
        )
        deleteCostInteractor.execute()
    }

    override fun onGoalDeleted(goal: Goal) {
        mView.onGoalDeleted(goal)
    }

    override fun swapGoalPositions(goal1: Goal, goal2: Goal) {
        val swapGoalPositionsInteractor = SwapGoalPositionsInteractorImpl(
                mExecutor,
                mMainThread,
                mGoalRepository,
                this,
                goal1,
                goal2
        )
        swapGoalPositionsInteractor.execute()
    }

    override fun onGoalPositionsSwapped() {
    }
}