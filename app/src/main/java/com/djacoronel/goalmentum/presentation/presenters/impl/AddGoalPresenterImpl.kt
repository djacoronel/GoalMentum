package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.AddGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.AddMilestoneInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.goal.AddGoalInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.AddMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.AddGoalPresenter


/**
 * Created by djacoronel on 10/9/17.
 */
class AddGoalPresenterImpl(
        executor: Executor,
        mainThread: MainThread,
        private val mView: AddGoalPresenter.View,
        private val mGoalRepository: GoalRepository,
        private val mMilestoneRepository: MilestoneRepository
) : AbstractPresenter(executor, mainThread), AddGoalPresenter, AddGoalInteractor.Callback, AddMilestoneInteractor.Callback {

    override fun addNewGoal(description: String, duration: String) {
        val addGoalInteractor = AddGoalInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mGoalRepository,
                description,
                duration)
        addGoalInteractor.execute()
    }

    override fun onGoalAdded(goalId: Long) {
        mView.onGoalAdded(goalId)
    }

    override fun addNewGeneralMilestone(goalId: Long) {
        val addMilestoneInteractor = AddMilestoneInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mMilestoneRepository,
                goalId,
                "General"
        )
        addMilestoneInteractor.execute()
    }

    override fun onMilestoneAdded(milestone: Milestone) {
    }
}