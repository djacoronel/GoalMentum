package com.djacoronel.goalmentum.domain.interactors.base.goal

import com.djacoronel.goalmentum.domain.interactors.base.Interactor
import com.djacoronel.goalmentum.domain.model.Goal


/**
 * Created by djacoronel on 10/6/17.
 * <p/>
 * This interactor is responsible for retrieving a list of costs from the database.
 */
interface GetAllGoalsInteractor : Interactor {

    interface Callback {
        fun onGoalsRetrieved(goalList: List<Goal>)
    }
}