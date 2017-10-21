package com.djacoronel.goalmentum.domain.interactors.base.work

import com.djacoronel.goalmentum.domain.interactors.base.Interactor
import com.djacoronel.goalmentum.domain.model.Work

/**
 * Created by djacoronel on 10/6/17.
 */
interface AddWorkInteractor : Interactor {

    interface Callback {
        fun onWorkAdded(work: Work)
    }
}