package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work

/**
 * Created by djacoronel on 10/9/17.
 */
interface ViewGoalPresenter {

    interface View {

        fun onGoalRetrieved(goal: Goal)

        fun onMilestoneRetrieved(milestone: Milestone)

        fun showMilestones(milestones: List<Milestone>, displayedWorks: HashMap<Long, List<Work>>)

        fun onClickAddMilestone()

        fun onMilestoneAdded(milestone: Milestone)

        fun onClickEditMilestone(milestone: Milestone)

        fun onMilestoneUpdated(milestone: Milestone)

        fun onClickDeleteMilestone(milestoneId: Long)

        fun onMilestoneDeleted(milestoneId: Long)

        fun onExpandMilestone(milestoneId: Long)

        fun onClickToggleWork(work: Work)

        fun onWorkToggled(work: Work)
    }

    fun getGoalById(goalId: Long)

    fun getMilestoneById(milestoneId: Long)

    fun addNewMilestone(goalId: Long, description: String)

    fun getAllMilestonesByAssignedGoal(goalId: Long)

    fun updateMilestone(milestone: Milestone)

    fun deleteMilestone(milestoneId: Long)

    fun toggleWork(work: Work)
}