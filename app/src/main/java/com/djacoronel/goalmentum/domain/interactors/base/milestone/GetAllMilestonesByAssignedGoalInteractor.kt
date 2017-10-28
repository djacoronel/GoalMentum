package com.djacoronel.goalmentum.domain.interactors.base.milestone

import com.djacoronel.goalmentum.domain.interactors.base.Interactor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work


/**
 * Created by djacoronel on 10/6/17.
 */
interface GetAllMilestonesByAssignedGoalInteractor : Interactor {

    interface Callback {
        fun onMilestonesRetrieved(milestones: List<Milestone>, displayedWorks: HashMap<Long, List<Work>>)
        fun noMilestonesFound()
    }
}