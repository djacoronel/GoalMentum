package com.djacoronel.goalmentum.presentation.presenters.impl

import com.db.chart.model.LineSet
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.GetWeeklyLineGraphInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.GetWeeklyLineGraphInteractorImpl
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.AnalyzeGoalsPresenter
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter

/**
 * Created by djacoronel on 10/31/17.
 */

class AnalyzeGoalPresenterImpl(
        executor: Executor,
        mainThread: MainThread,
        private val view: AnalyzeGoalsPresenter.View,
        private val goalRepository: GoalRepository,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository
) : AbstractPresenter(executor, mainThread), AnalyzeGoalsPresenter,
        GetWeeklyLineGraphInteractor.Callback
{
    override fun getWeeklyLineGraph() {
        val getWeeklyLineGraphInteractor = GetWeeklyLineGraphInteractorImpl(
                mExecutor,
                mMainThread,
                workRepository,
                this
        )
        getWeeklyLineGraphInteractor.execute()
    }

    override fun onWeeklyLineGraphRetrieved(lineSet: LineSet) {
        view.onWeeklyLineGraphRetrieved(lineSet)
    }

    override fun getWeeklyBarGraph() {
    }

    override fun getAnalysis() {
    }
}