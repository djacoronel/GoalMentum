package com.djacoronel.goalmentum.presentation.presenters.impl

import com.db.chart.model.Bar
import com.db.chart.model.Point
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.GetAnalysisDataInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetBarGraphDataInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetLineGraphDataInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.GetAnalysisDataInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.GetBarGraphDataInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.GetLineGraphDataInteractorImpl
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.presentation.presenters.AbstractPresenter
import com.djacoronel.goalmentum.presentation.presenters.AnalyzeGoalsPresenter

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
        GetLineGraphDataInteractor.Callback, GetBarGraphDataInteractor.Callback,
        GetAnalysisDataInteractor.Callback {
    override fun getLineGraphData() {
        val getWeeklyLineGraphInteractor = GetLineGraphDataInteractorImpl(
                mExecutor,
                mMainThread,
                workRepository,
                this
        )
        getWeeklyLineGraphInteractor.execute()
    }

    override fun onLineGraphDataRetrieved(currentWeekData: List<Pair<String, Int>>, previousWeekData: List<Pair<String, Int>>) {
        view.onWeeklyLineGraphRetrieved(currentWeekData, previousWeekData)
    }

    override fun getBarGraphData() {
        val getBarGraphDataInteractor = GetBarGraphDataInteractorImpl(
                mExecutor,
                mMainThread,
                goalRepository,
                milestoneRepository,
                workRepository,
                this
        )
        getBarGraphDataInteractor.execute()
    }

    override fun onBarGraphDataRetrieved(barData: List<Pair<String,Int>>) {
        view.onWeeklyBarGraphRetrieved(barData)
    }

    override fun getAnalysisData() {
        val getAnalysisInteractor = GetAnalysisDataInteractorImpl(
                mExecutor,
                mMainThread,
                goalRepository,
                milestoneRepository,
                workRepository,
                this
        )
        getAnalysisInteractor.execute()
    }

    override fun onAnalysisDataRetrieved(data: List<Int>) {
        view.onAnalysisRetrieved(data)
    }
}