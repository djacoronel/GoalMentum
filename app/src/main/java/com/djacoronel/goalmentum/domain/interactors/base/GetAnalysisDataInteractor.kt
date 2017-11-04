package com.djacoronel.goalmentum.domain.interactors.base

/**
 * Created by djacoronel on 10/31/17.
 */
interface GetAnalysisDataInteractor : Interactor {
    interface Callback {
        fun onAnalysisDataRetrieved(data: List<Int>)
    }
}