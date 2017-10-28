package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetMilestoneByIdInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.AddWorkInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.GetAllWorksByAssignedMilestoneInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.work.AddWorkInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.work.GetWorksByAssignedMilestoneInteractorImpl
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
        private val mMilestoneRepository: MilestoneRepository,
        private val mWorkRepository: WorkRepository
) : AbstractPresenter(executor, mainThread), AddWorkPresenter,
        GetAllWorksByAssignedMilestoneInteractor.Callback,
        AddWorkInteractor.Callback,
        GetMilestoneByIdInteractor.Callback
{

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

    override fun getMilestoneById(milestoneId: Long) {
        val getMilestoneByIdInteractor = GetMilestoneByIdInteractorImpl(
                mExecutor,
                mMainThread,
                milestoneId,
                mMilestoneRepository,
                this
        )

        getMilestoneByIdInteractor.execute()
    }

    override fun onMilestoneRetrieved(milestone: Milestone) {
        mView.onMilestoneRetrieved(milestone)
    }

    override fun noMilestoneFound() {

    }
}