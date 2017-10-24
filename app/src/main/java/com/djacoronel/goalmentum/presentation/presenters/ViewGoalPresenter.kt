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

        fun showMilestones(milestones: List<Milestone>)

        fun onClickAddMilestone()

        fun onMilestoneAdded(milestone: Milestone)

        fun onClickEditMilestone(milestone: Milestone)

        fun onMilestoneUpdated(milestone: Milestone)

        fun onClickDeleteMilestone(milestoneId: Long)

        fun onMilestoneDeleted(milestoneId: Long)

        fun onExpandMilestone(milestoneId: Long)


        fun showWorks(milestoneId: Long, works: List<Work>)

        fun onClickAddWork(milestoneId: Long)

        fun onWorkAdded(work: Work)

        fun onClickEditWork(work: Work)

        fun onWorkUpdated(work: Work)

        fun onClickDeleteWork(work: Work)

        fun onWorkDeleted(work: Work)

        fun onClickToggleWork(work: Work)


        fun onAllMilestonesAchieved()

        fun onGoalAchieved(goal: Goal)
    }

    fun addNewMilestone(goalId: Long, description: String)

    fun getAllMilestonesByAssignedGoal(goalId: Long)

    fun updateMilestone(milestone: Milestone)

    fun deleteMilestone(milestoneId: Long)

    fun addNewWork(milestoneId: Long, description: String)

    fun getAllWorkByAssignedMilestone(milestoneId: Long)

    fun updateWork(work: Work)

    fun deleteWork(work: Work)

    fun toggleMilestoneAchieveStatus(milestone: Milestone, works: List<Work>)

    fun updateGoalMomentum(goalId: Long, momentum: Int)

    fun achieveGoal(goalId: Long)
}