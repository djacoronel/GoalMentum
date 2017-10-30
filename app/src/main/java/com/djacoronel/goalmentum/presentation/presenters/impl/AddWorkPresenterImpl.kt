package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetMilestoneByIdInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.*
import com.djacoronel.goalmentum.domain.interactors.impl.work.*
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.AddWorkPresenter
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter
import com.djacoronel.milestonementum.domain.interactors.impl.milestone.GetMilestoneByIdInteractorImpl

/**
 * Created by djacoronel on 10/28/17.
 */

class AddWorkPresenterImpl(
        executor: Executor,
        mainThread: MainThread,
        private val mView: AddWorkPresenter.View,
        private val mGoalRepository: GoalRepository,
        private val mMilestoneRepository: MilestoneRepository,
        private val mWorkRepository: WorkRepository
) : AbstractPresenter(executor, mainThread), AddWorkPresenter,
        GetMilestoneByIdInteractor.Callback,
        GetAllWorksByAssignedMilestoneInteractor.Callback,
        AddWorkInteractor.Callback,
        EditWorkInteractor.Callback,
        DeleteWorkInteractor.Callback,
        ToggleWorkAchieveStatusInteractor.Callback
{
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
        mView.onMilestoneRetrieved(milestone)
    }

    override fun noMilestoneFound() {

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
                mWorkRepository,
                this,
                work
        )
        editWorkInteractor.execute()
    }

    override fun onWorkUpdated(work: Work) {
        mView.onWorkUpdated(work)
    }


    override fun deleteWork(workId: Long) {
        val deleteWorkInteractor = DeleteWorkInteractorImpl(
                mExecutor,
                mMainThread,
                mWorkRepository,
                this,
                workId
        )
        deleteWorkInteractor.execute()
    }

    override fun onWorkDeleted(workId: Long) {
        mView.onWorkDeleted(workId)
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
}