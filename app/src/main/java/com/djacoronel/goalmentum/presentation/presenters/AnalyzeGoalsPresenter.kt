package com.djacoronel.goalmentum.presentation.presenters

import com.db.chart.model.Bar
import com.db.chart.model.Point

/**
 * Created by djacoronel on 10/9/17.
 */
interface AnalyzeGoalsPresenter {

    interface View{
        fun onWeeklyLineGraphRetrieved(dataPoints: List<Point>)

        fun onWeeklyBarGraphRetrieved(dataBars: List<Bar>)

        fun onAnalysisRetrieved(data: List<Int>)
    }

    fun getWeeklyLineGraph()

    fun getWeeklyBarGraph()

    fun getAnalysis()
}