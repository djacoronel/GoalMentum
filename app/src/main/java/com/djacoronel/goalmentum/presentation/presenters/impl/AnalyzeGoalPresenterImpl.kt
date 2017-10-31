package com.djacoronel.goalmentum.presentation.presenters.impl

import com.db.chart.model.Bar
import com.db.chart.model.Point
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.GetAnalysisInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetWeeklyLineGraphInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetAllGoalsInteractor
import com.djacoronel.goalmentum.domain.interactors.impl.GetAnalysisInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.GetWeeklyLineGraphInteractorImpl
import com.djacoronel.goalmentum.domain.interactors.impl.goal.GetAllGoalsInteractorImpl
import com.djacoronel.goalmentum.domain.model.Goal
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
        GetWeeklyLineGraphInteractor.Callback, GetAllGoalsInteractor.Callback ,
        GetAnalysisInteractor.Callback
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

    override fun onWeeklyLineGraphRetrieved(dataPoints: List<Point>) {
        view.onWeeklyLineGraphRetrieved(dataPoints)
    }

    override fun getWeeklyBarGraph() {
        val getAllGoalsInteractor = GetAllGoalsInteractorImpl(
                mExecutor,
                mMainThread,
                goalRepository,
                milestoneRepository,
                workRepository,
                this
        )
        getAllGoalsInteractor.execute()
    }

    override fun onGoalsRetrieved(goalList: List<Goal>) {
        val sortedGoalList = goalList.sortedBy { it.achievedWork }
        val dataBars = mutableListOf<Bar>()
        val index = sortedGoalList.lastIndex

        for (i in 0..6){
            if (i <= index)
                dataBars.add(Bar(sortedGoalList[i].description, sortedGoalList[i].achievedWork.toFloat()))
            else
                dataBars.add(0,Bar("",0f))
        }

        view.onWeeklyBarGraphRetrieved(dataBars)
    }

    override fun getAnalysis() {
        val getAnalysisInteractor = GetAnalysisInteractorImpl(
                mExecutor,
                mMainThread,
                goalRepository,
                milestoneRepository,
                workRepository,
                this
        )
        getAnalysisInteractor.execute()
    }

    override fun onAnalysisRetrieved(data: List<Int>) {
        view.onAnalysisRetrieved(data)
    }
}