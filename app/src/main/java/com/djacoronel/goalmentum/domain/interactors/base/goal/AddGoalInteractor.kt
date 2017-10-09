package com.djacoronel.goalmentum.domain.interactors.base.goal

import com.djacoronel.goalmentum.domain.interactors.base.Interactor



/**
 * Created by djacoronel on 10/6/17.
 */
interface AddGoalInteractor : Interactor {

    interface Callback {
        fun onGoalAdded()
    }
}