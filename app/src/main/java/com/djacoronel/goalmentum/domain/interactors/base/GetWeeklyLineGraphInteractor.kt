package com.djacoronel.goalmentum.domain.interactors.base

import com.db.chart.model.LineSet

/**
 * Created by djacoronel on 10/31/17.
 */
interface GetWeeklyLineGraphInteractor : Interactor {
    interface Callback{
        fun onWeeklyLineGraphRetrieved(lineSet: LineSet)
    }
}