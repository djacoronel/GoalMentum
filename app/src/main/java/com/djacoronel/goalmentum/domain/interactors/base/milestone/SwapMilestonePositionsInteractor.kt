package com.djacoronel.goalmentum.domain.interactors.base.milestone

import com.djacoronel.goalmentum.domain.interactors.base.Interactor

/**
 * Created by djacoronel on 11/11/17.
 */
interface SwapMilestonePositionsInteractor : Interactor {

    interface Callback {
        fun onMilestonePositionsSwapped()
    }
}