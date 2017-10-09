package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.presentation.ui.BaseView

/**
 * Created by djacoronel on 10/9/17.
 */
interface ViewGoalPresenter : BasePresenter {

    interface View : BaseView {

        fun setGoalDescription(goalDesc: String)

        fun onMilestoneAdded()

        fun showMilestones(milestones: List<Milestone>)

        fun onClickMilestone(milestoneId: Long)

        fun onClickAddMilestone()

        fun onLongClickMilestone(milestoneId: Long)

        fun onMilestoneDeleted(milestone: Milestone)
    }

    fun getGoalById(goalId: Long)

    fun addNewMilestone(goalId: Long, description: String)

    fun getAllMilestonesByAssignedGoal(goalId: Long)

    fun deleteMilestone(milestoneId: Long)
}