package com.djacoronel.goalmentum.presentation.presenters

/**
 * Created by djacoronel on 10/9/17.
 */
interface AnalyzeGoalsPresenter {

    interface View{
        fun onWeeklyLineGraphRetrieved(
                currentWeekData: List<Pair<String, Int>>,
                previousWeekData: List<Pair<String, Int>>
        )

        fun onWeeklyBarGraphRetrieved(dataBars: List<Pair<String,Int>>)

        fun onAnalysisRetrieved(data: List<Int>)
    }

    fun getLineGraphData()

    fun getBarGraphData()

    fun getAnalysisData()
}