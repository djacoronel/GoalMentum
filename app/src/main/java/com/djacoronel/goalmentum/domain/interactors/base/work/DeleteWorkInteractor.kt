package com.djacoronel.goalmentum.domain.interactors.base.work

import com.djacoronel.goalmentum.domain.interactors.base.Interactor
import com.djacoronel.goalmentum.domain.model.Work


/**
 * Created by djacoronel on 10/6/17.
 */
interface DeleteWorkInteractor : Interactor {

    interface Callback {
        fun onWorkDeleted(workId: Long)
    }
}