package com.djacoronel.goalmentum.presentation.presenters

import com.db.chart.model.LineSet
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work

/**
 * Created by djacoronel on 10/9/17.
 */
interface AnalyzeGoalsPresenter {

    interface View{
        fun onWeeklyLineGraphRetrieved(lineSet: LineSet)

        fun onWeeklyBarGraphRetrieved()

        fun onAnalysisRetrieved()
    }

    fun getWeeklyLineGraph()

    fun getWeeklyBarGraph()

    fun getAnalysis()
}