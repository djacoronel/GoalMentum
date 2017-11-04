package com.djacoronel.goalmentum.domain.interactors.base

/**
 * Created by djacoronel on 10/31/17.
 */
interface GetLineGraphDataInteractor : Interactor {
    interface Callback{
        fun onLineGraphDataRetrieved(
                currentWeekData: List<Pair<String, Int>>,
                previousWeekData: List<Pair<String, Int>>
        )
    }
}