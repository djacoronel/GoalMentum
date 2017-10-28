package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.ui.BaseView

/**
 * Created by djacoronel on 10/9/17.
 */
interface AddWorkPresenter : BasePresenter {

    interface View : BaseView {

        fun showWorks(milestoneId: Long, works: List<Work>)

        fun onClickAddWork(workDescription: String)

        fun onWorkAdded(work: Work)
    }

    fun addNewWork(milestoneId: Long, description: String)

    fun getAllWorkByAssignedMilestone(milestoneId: Long)
}