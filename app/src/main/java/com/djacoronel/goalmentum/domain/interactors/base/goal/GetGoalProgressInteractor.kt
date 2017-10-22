package com.djacoronel.goalmentum.domain.interactors.base.goal

import com.djacoronel.goalmentum.domain.interactors.base.Interactor
import com.djacoronel.goalmentum.domain.model.Goal

/**
 * Created by djacoronel on 10/22/17.
 */
interface GetGoalProgressInteractor : Interactor{

    interface Callback {
        fun onGoalProgressRetrieved(info: Map<Goal,Array<Int>>)
    }
}