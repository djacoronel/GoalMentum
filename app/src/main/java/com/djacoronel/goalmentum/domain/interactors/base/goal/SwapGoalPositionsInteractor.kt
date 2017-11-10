package com.djacoronel.goalmentum.domain.interactors.base.goal

import com.djacoronel.goalmentum.domain.interactors.base.Interactor

/**
 * Created by djacoronel on 11/11/17.
 */
interface SwapGoalPositionsInteractor : Interactor {

    interface Callback {
        fun onGoalPositionsSwapped()
    }
}