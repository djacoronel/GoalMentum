package com.djacoronel.goalmentum.domain.interactors.base


/**
 * Created by djacoronel on 10/31/17.
 */

interface GetBarGraphDataInteractor : Interactor {
    interface Callback{
        fun onBarGraphDataRetrieved(barData: List<Pair<String,Int>>)
    }
}