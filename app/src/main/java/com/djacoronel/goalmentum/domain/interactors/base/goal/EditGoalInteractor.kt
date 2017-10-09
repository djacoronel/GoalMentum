package com.djacoronel.goalmentum.domain.interactors.base.goal

import com.djacoronel.goalmentum.domain.interactors.base.Interactor
import com.djacoronel.goalmentum.domain.model.Goal


/**
 * Created by djacoronel on 10/6/17.
 */
interface EditGoalInteractor : Interactor {

    interface Callback {

        fun onGoalUpdated(cost: Goal)
    }
}