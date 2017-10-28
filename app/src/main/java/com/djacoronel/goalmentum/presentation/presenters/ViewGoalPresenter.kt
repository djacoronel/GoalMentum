package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.ui.BaseView

/**
 * Created by djacoronel on 10/9/17.
 */
interface ViewGoalPresenter : BasePresenter {

    interface View : BaseView {

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


        fun onAllMilestonesAchieved()

        fun onGoalAchieved(goal: Goal)
    }

    fun addNewMilestone(goalId: Long, description: String)

    fun getAllMilestonesByAssignedGoal(goalId: Long)

    fun updateMilestone(milestone: Milestone)

    fun deleteMilestone(milestoneId: Long)

    fun toggleMilestoneAchieveStatus(milestoneId: Long)

    fun updateGoalMomentum(goalId: Long, momentum: Int)

    fun achieveGoal(goalId: Long)

    fun toggleWork(work: Work)
}