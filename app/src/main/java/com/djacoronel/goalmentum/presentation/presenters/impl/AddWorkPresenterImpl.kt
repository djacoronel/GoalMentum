package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.work.AddWorkInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.GetAllWorksByAssignedMilestoneInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.work.AddWorkInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.work.GetWorksByAssignedMilestoneInteractorImpl
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.AddWorkPresenter
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter

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
        AddWorkInteractor.Callback
{
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
}