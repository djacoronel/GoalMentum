package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetAllGoalsInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.SwapGoalPositionsInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetAllGoalsInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.SwapGoalPositionsInteractorImpl
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.MainPresenter
import javax.inject.Inject


/**
 * Created by djacoronel on 10/7/17.
 */
class MainPresenterImpl @Inject constructor(
        executor: Executor,
        mainThread: MainThread,
        val mView: MainPresenter.View,
        val mGoalRepository: GoalRepository,
        val mMilestoneRepository: MilestoneRepository,
        val mWorkRepository: WorkRepository
) : AbstractPresenter(executor, mainThread), MainPresenter,
        GetAllGoalsInteractor.Callback,
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