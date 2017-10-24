package com.djacoronel.goalmentum.domain.interactors.base.goal

import com.djacoronel.goalmentum.domain.interactors.base.Interactor
import com.djacoronel.goalmentum.domain.model.Goal

/**
 * Created by djacoronel on 10/6/17.
 */
interface GetGoalByIdAndUpdateMomentumInteractor : Interactor {

    interface Callback {
        fun onGoalMomentumUpdated(goal: Goal)
    }
}