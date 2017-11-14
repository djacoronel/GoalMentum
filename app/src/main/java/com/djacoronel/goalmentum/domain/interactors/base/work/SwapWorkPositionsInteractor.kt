package com.djacoronel.goalmentum.domain.interactors.base.work

import com.djacoronel.goalmentum.domain.interactors.base.Interactor

/**
 * Created by djacoronel on 11/11/17.
 */
interface SwapWorkPositionsInteractor : Interactor {

    interface Callback {
        fun onWorkPositionsSwapped()
    }
}