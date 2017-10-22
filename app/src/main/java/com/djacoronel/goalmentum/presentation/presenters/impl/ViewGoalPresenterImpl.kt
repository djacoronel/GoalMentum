package com.djacoronel.goalmentum.presentation.presenters.impl

import android.util.Log
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.milestone.*
import com.djacoronel.goalmentum.domain.interactors.base.work.AddWorkInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.DeleteWorkInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.EditWorkInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.GetAllWorksByAssignedMilestoneInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.AddMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.DeleteMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.EditMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.milestone.GetAllMilestonesByAssignedGoalInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.work.AddWorkInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.work.DeleteWorkInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.work.EditWorkInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.work.GetWorksByAssignedMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
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
        private val mMilestoneRepository: MilestoneRepository,
        private val mWorkRepository: WorkRepository
) : AbstractPresenter(executor, mainThread), ViewGoalPresenter,
        AddMilestoneInteractor.Callback,
        EditMilestoneInteractor.Callback,
        GetAllMilestonesByAssignedGoalInteractor.Callback,
        DeleteMilestoneInteractor.Callback,
        AddWorkInteractor.Callback,
        EditWorkInteractor.Callback,
        GetAllWorksByAssignedMilestoneInteractor.Callback,
        DeleteWorkInteractor.Callback {

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
                this,
                mMilestoneRepository,
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
                milestoneId,
                this,
                mMilestoneRepository
        )
        deleteCostInteractor.execute()
    }

    override fun onMilestoneDeleted(milestoneId: Long) {
        mView.onMilestoneDeleted(milestoneId)
    }


    override fun getAllWorkByAssignedMilestone(milestoneId: Long) {
        val getWorksInteractor = GetWorksByAssignedMilestoneInteractorImpl(
                mExecutor,
                mMainThread,
                mWorkRepository,
                this,
                milestoneId
        )
        getWorksInteractor.execute()
    }

    override fun onWorksRetrieved(milestoneId: Long, works: List<Work>) {
        mView.showWorks(milestoneId, works)
    }


    override fun addNewWork(milestoneId: Long, description: String) {
        val addWorkInteractor = AddWorkInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mWorkRepository,
                milestoneId,
                description
        )
        addWorkInteractor.execute()
    }

    override fun onWorkAdded(work: Work) {
        mView.onWorkAdded(work)
    }


    override fun updateWork(work: Work) {
        val editWorkInteractor = EditWorkInteractorImpl(
                mExecutor,
                mMainThread,
                this,
                mWorkRepository,
                work
        )
        editWorkInteractor.execute()
    }

    override fun onWorkUpdated(work: Work) {
        mView.onWorkUpdated(work)
    }


    override fun deleteWork(work: Work) {
        val deleteWorkInteractor = DeleteWorkInteractorImpl(
                mExecutor,
                mMainThread,
                work,
                this,
                mWorkRepository
        )
        deleteWorkInteractor.execute()
    }

    override fun onWorkDeleted(work: Work) {
        mView.onWorkDeleted(work)
    }

    override fun toggleMilestoneAchieveStatus(milestone: Milestone, works: List<Work>) {
        Log.i(milestone.achieved.toString(),works.toString())

        val worksAchieved = works.filter { it.achieved == true }

        if ( worksAchieved.size == works.size-1 && milestone.achieved == false) {
            milestone.achieved = true
            updateMilestone(milestone)
        } else if (worksAchieved.size!= works.size-1 && milestone.achieved == true) {
            milestone.achieved = false
            updateMilestone(milestone)
        }
    }
}