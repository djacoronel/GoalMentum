package com.djacoronel.goalmentum.domain.interactors.base.milestone

import com.djacoronel.goalmentum.domain.interactors.base.Interactor
import com.djacoronel.goalmentum.domain.model.Milestone


/**
 * Created by djacoronel on 10/6/17.
 */
interface GetMilestoneByIdInteractor : Interactor {

    interface Callback {
        fun onMilestoneRetrieved(milestone: Milestone)

        fun noMilestoneFound()
    }
}