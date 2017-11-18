package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.DeleteGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.SwapGoalPositionsInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.*
import com.djacoronel.goalmentum.domain.interactors.base.work.GetAllWorksByAssignedMilestoneInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.ToggleWorkAchieveStatusInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.goal.DeleteGoalInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.EditGoalInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetGoalByIdInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.SwapGoalPositionsInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.*
import com.djacoronel.goalmentum.domain.interactors.impl.work.GetWorksByAssignedMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.work.ToggleWorkAchieveStatusInteractorImpl
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter
import javax.inject.Inject

/**
 * Created by djacoronel on 10/9/17.
 */

class GoalPresenterImpl @Inject constructor(
        executor: Executor,
        mainThread: MainThread,
        private val mView: GoalPresenter.View,
        private val mGoalRepository: GoalRepository,
        private val mMilestoneRepository: MilestoneRepository,
        private val mWorkRepository: WorkRepository
) : AbstractPresenter(executor, mainThread), GoalPresenter,
        GetGoalByIdInteractor.Callback,
        DeleteGoalInteractor.Callback,
        EditGoalInteractor.Callback,
        GetMilestoneByIdInteractor.Callback,
        AddMilestoneInteractor.Callback,
        EditMilestoneInteractor.Callback,
        GetAllMilestonesByAssignedGoalInteractor.Callback,
        DeleteMilestoneInteractor.Callback,
        GetAllWorksByAssignedMilestoneInteractor.Callback,
        ToggleWorkAchieveStatusInteractor.Callback,
        SwapMilestonePositionsInteractor.Callback
{

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

    override fun deleteGoal(goal: Goal) {
        val deleteGoalInteractor = DeleteGoalInteractorImpl(
                mExecutor,
                mMainThread,
                mGoalRepository,
                this,
                goal
        )
        deleteGoalInteractor.execute()
    }

    override fun onGoalDeleted(goal: Goal) {
        mView.onGoalDeleted(goal)
    }

    override fun updateGoal(goal: Goal) {
        val editGoalInteractor = EditGoalInteractorImpl(
                mExecutor,
                mMainThread,
                mGoalRepository,
                this,
                goal
        )
        editGoalInteractor.execute()
    }

    override fun onGoalUpdated(goal: Goal) {
        mView.onGoalUpdated(goal)
    }


    override fun getMilestoneById(milestoneId: Long) {
        val getMilestoneByIdInteractor = GetMilestoneByIdInteractorImpl(
                mExecutor,
                mMainThread,
                mMilestoneRepository,
                mWorkRepository,
                this,
                milestoneId
        )
        getMilestoneByIdInteractor.execute()
    }

    override fun onMilestoneRetrieved(milestone: Milestone) {
        mView.onMilestoneUpdated(milestone)
    }

    override fun noMilestoneFound() {
    }


    override fun getAllMilestonesByAssignedGoal(goalId: Long) {
        val getMilestonesInteractor = GetAllMilestonesByAssignedGoalInteractorImpl(
                mExecutor,
                mMainThread,
                mMilestoneRepository,
                mWorkRepository,
                this,
                goalId
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


    override fun getAllWorkByAssignedMilestone(milestoneId: Long) {
        val getAllWorksByAssignedMilestoneInteractor = GetWorksByAssignedMilestoneInteractorImpl(
                mExecutor,
                mMainThread,
                mWorkRepository,
                this,
                milestoneId
        )
        getAllWorksByAssignedMilestoneInteractor.execute()
    }

    override fun onWorksRetrieved(milestoneId: Long, works: List<Work>) {
        val activeWorks = works.filter { it.achieved == false }
        val newDisplayedWorks = if (activeWorks.size < 3) activeWorks else activeWorks.subList(0, 3)
        mView.onNewDisplayedWorksRetrieved(milestoneId, newDisplayedWorks)
    }

    override fun toggleWork(work: Work) {
        val toggleWorkAchieveStatusInteractor = ToggleWorkAchieveStatusInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mGoalRepository,
                mMilestoneRepository,
                mWorkRepository,
                work
        )
        toggleWorkAchieveStatusInteractor.execute()
    }

    override fun onWorkAchieveStatusToggled(work: Work) {
        mView.onWorkToggled(work)
    }

    override fun swapMilestonePositions(milestone1: Milestone, milestone2: Milestone) {
        val swapMilestonePositionsInteractor = SwapMilestonePositionsInteractorImpl(
                mExecutor,
                mMainThread,
                mMilestoneRepository,
                this,
                milestone1,
                milestone2
        )

        swapMilestonePositionsInteractor.execute()
    }

    override fun onMilestonePositionsSwapped() {
    }
}