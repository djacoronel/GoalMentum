package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.AddMilestoneInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.DeleteMilestoneInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetAllMilestonesByAssignedGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetGoalByIdInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.AddMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.DeleteMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.GetAllMilestonesByAssignedGoalInteractorImpl
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter

/**
 * Created by djacoronel on 10/9/17.
 */
class ViewGoalPresenterImpl(
        executor: Executor,
        mainThread: MainThread,
        private val mView: ViewGoalPresenter.View,
        private val mMilestoneRepository: MilestoneRepository
        ) : AbstractPresenter(executor, mainThread), ViewGoalPresenter,
        AddMilestoneInteractor.Callback,
        GetAllMilestonesByAssignedGoalInteractor.Callback,
        DeleteMilestoneInteractor.Callback {

    override fun resume() {

    }

    override fun pause() {

    }

    override fun stop() {

    }

    override fun destroy() {

    }

    override fun onError(message: String) {

    }

    override fun addNewMilestone(goalId: Long, description: String) {
        val addMilestoneInteractor = AddMilestoneInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mMilestoneRepository,
                goalId,
                description
        )
        addMilestoneInteractor.execute()
    }

    override fun onMilestoneAdded() {
        mView.onMilestoneAdded()
    }

    override fun getAllMilestonesByAssignedGoal(goalId: Long) {
        val getMilestonesInteractor = GetAllMilestonesByAssignedGoalInteractorImpl(
                mExecutor,
                mMainThread,
                goalId,
                mMilestoneRepository,
                this
        )
        getMilestonesInteractor.execute()
    }

    override fun onMilestonesRetrieved(milestones: List<Milestone>) {
        mView.showMilestones(milestones)
    }

    override fun noMilestonesFound() {

    }

    override fun deleteMilestone(milestoneId: Long) {
        val deleteCostInteractor = DeleteMilestoneInteractorImpl(
                mExecutor,
                mMainThread,
                milestoneId,
                this,
                mMilestoneRepository
        )
        deleteCostInteractor.execute()
    }

    override fun onMilestoneDeleted(milestone: Milestone) {
        mView.onMilestoneDeleted(milestone)
    }
}