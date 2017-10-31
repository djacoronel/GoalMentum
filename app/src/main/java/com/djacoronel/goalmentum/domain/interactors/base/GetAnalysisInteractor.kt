package com.djacoronel.goalmentum.domain.interactors.base

/**
 * Created by djacoronel on 10/31/17.
 */
interface GetAnalysisInteractor : Interactor {
    interface Callback {
        fun onAnalysisRetrieved(data: List<Int>)
    }
}