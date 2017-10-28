package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work

/**
 * Created by djacoronel on 10/9/17.
 */
interface AddWorkPresenter {

    interface View {

        fun onMilestoneRetrieved(milestone: Milestone)

        fun showWorks(milestoneId: Long, works: List<Work>)

        fun onClickAddWork(workDescription: String)

        fun onWorkAdded(work: Work)
    }

    fun getMilestoneById(milestoneId: Long)

    fun addNewWork(milestoneId: Long, description: String)

    fun getAllWorkByAssignedMilestone(milestoneId: Long)
}