package com.djacoronel.goalmentum.presentation.presenters.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.AddGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.goal.AddGoalInteractorImpl
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.AddGoalPresenter


/**
 * Created by djacoronel on 10/9/17.
 */
class AddGoalPresenterImpl(
        executor: Executor,
        mainThread: MainThread,
        private val mView: AddGoalPresenter.View,
        private val mGoalRepository: GoalRepository
) : AbstractPresenter(executor, mainThread), AddGoalPresenter, AddGoalInteractor.Callback {


    override fun addNewGoal(description: String, duration: String) {
        val addGoalInteractor = AddGoalInteractorImpl(mExecutor,
                mMainThread,
                this,
                mGoalRepository,
                description,
                duration)
        addGoalInteractor.execute()
    }

    override fun onGoalAdded() {
        mView.onGoalAdded()
    }


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
}