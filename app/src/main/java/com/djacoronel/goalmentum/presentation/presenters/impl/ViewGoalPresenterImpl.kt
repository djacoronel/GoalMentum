package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdAndSetAchievedInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdAndUpdateMomentumInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.*
import com.djacoronel.goalmentum.domain.interactors.base.work.ToggleWorkAchieveStatusInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetGoalByIdAndSetAchievedInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetGoalByIdAndUpdateMomentumImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetGoalByIdInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.*
import com.djacoronel.goalmentum.domain.interactors.impl.work.ToggleWorkAchieveStatusInteractorImpl
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter

/**
 * Created by djacoronel on 10/9/17.
 */
class ViewGoalPresenterImpl(
        executor: Executor,
        mainThread: MainThread,
        private val mView: ViewGoalPresenter.View,
        private val mGoalRepository: GoalRepository,
        private val mMilestoneRepository: MilestoneRepository,
        private val mWorkRepository: WorkRepository
) : AbstractPresenter(executor, mainThread), ViewGoalPresenter,
        GetGoalByIdInteractor.Callback,
        AddMilestoneInteractor.Callback,
        EditMilestoneInteractor.Callback,
        GetAllMilestonesByAssignedGoalInteractor.Callback,
        DeleteMilestoneInteractor.Callback,
        GetGoalByIdAndUpdateMomentumInteractor.Callback,
        GetGoalByIdAndSetAchievedInteractor.Callback,
        ToggleMilestoneAchieveStatusInteractor.Callback,
        ToggleWorkAchieveStatusInteractor.Callback {

    override fun getGoalById(goalId: Long) {
        val getGoalByIdInteractor = GetGoalByIdInteractorImpl(
                mExecutor,
                mMainThread,
                mGoalRepository,
                mMilestoneRepository,
                mWorkRepository,
                this,
                goalId
        )
        getGoalByIdInteractor.execute()
    }

    override fun onGoalRetrieved(goal: Goal) {
        mView.onGoalRetrieved(goal)
    }

    override fun noGoalFound() {
    }

    override fun getAllMilestonesByAssignedGoal(goalId: Long) {
        val getMilestonesInteractor = GetAllMilestonesByAssignedGoalInteractorImpl(
                mExecutor,
                mMainThread,
                goalId,
                mMilestoneRepository,
                mWorkRepository,
                this
        )
        getMilestonesInteractor.execute()
    }

    override fun onMilestonesRetrieved(milestones: List<Milestone>, displayedWorks: HashMap<Long, List<Work>>) {
        mView.showMilestones(milestones, displayedWorks)
    }

    override fun noMilestonesFound() {
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

    override fun onMilestoneAdded(milestone: Milestone) {
        mView.onMilestoneAdded(milestone)
    }


    override fun updateMilestone(milestone: Milestone) {
        val editMilestoneInteractor = EditMilestoneInteractorImpl(
                mExecutor,
                mMainThread,
                mMilestoneRepository,
                this,
                milestone
        )
        editMilestoneInteractor.execute()
    }

    override fun onMilestoneUpdated(milestone: Milestone) {
        mView.onMilestoneUpdated(milestone)
    }


    override fun deleteMilestone(milestoneId: Long) {
        val deleteCostInteractor = DeleteMilestoneInteractorImpl(
                mExecutor,
                mMainThread,
                mMilestoneRepository,
                this,
                milestoneId
        )
        deleteCostInteractor.execute()
    }

    override fun onMilestoneDeleted(milestoneId: Long) {
        mView.onMilestoneDeleted(milestoneId)
    }


    override fun toggleWork(work: Work) {
        val toggleWorkAchieveStatusInteractor = ToggleWorkAchieveStatusInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mWorkRepository,
                work
        )
        toggleWorkAchieveStatusInteractor.execute()
    }

    override fun onWorkAchieveStatusToggled(work: Work) {
        mView.onWorkToggled(work)
    }


    override fun toggleMilestoneAchieveStatus(milestoneId: Long) {
        val toggleMilestoneAchieveStatusInteractor = ToggleMilestoneAchieveStatusInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mMilestoneRepository,
                mWorkRepository,
                milestoneId
        )
        toggleMilestoneAchieveStatusInteractor.execute()
    }

    override fun onMilestoneAchieveStatusUpdated(milestone: Milestone) {
        mView.onMilestoneAchieved(milestone)
    }


    override fun updateGoalMomentum(goalId: Long, momentum: Int) {
        val getGoalByIdAndUpdateMomentumInteractor = GetGoalByIdAndUpdateMomentumImpl(
                mExecutor,
                mMainThread,
                this,
                mGoalRepository,
                goalId,
                momentum
        )
        getGoalByIdAndUpdateMomentumInteractor.execute()
    }

    override fun onGoalMomentumUpdated(goal: Goal) {
        mView.onGoalMomentumUpdated(goal)
    }


    override fun achieveGoal(goalId: Long) {
        val getGoalByIdAndSetAchievedInteractor = GetGoalByIdAndSetAchievedInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mGoalRepository,
                mMilestoneRepository,
                goalId
        )

        getGoalByIdAndSetAchievedInteractor.execute()
    }

    override fun onGoalAchieved(goal: Goal) {
        mView.onGoalAchieved(goal)
    }
}