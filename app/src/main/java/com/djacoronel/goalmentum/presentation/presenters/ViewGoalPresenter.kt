package com.djacoronel.goalmentum.presentation.presenters

import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.ui.BaseView

/**
 * Created by djacoronel on 10/9/17.
 */
interface ViewGoalPresenter : BasePresenter {

    interface View : BaseView {

        fun onMilestoneAdded()

        fun showMilestones(milestones: List<Milestone>)

        fun onClickAddMilestone()

        fun onClickEditMilestone(milestone: Milestone)

        fun onClickDeleteMilestone(milestoneId: Long)

        fun onExpandMilestone(milestoneId: Long)


        fun onClickAddWork(milestoneId: Long)

        fun onWorkAdded(milestoneId: Long)

        fun showWork(milestoneId: Long, works: List<Work>)

        fun onClickEditWork(work: Work)

        fun onClickDeleteWork(workId: Long)

        fun onClickToggleWork(work: Work)
    }

    fun addNewMilestone(goalId: Long, description: String)

    fun getAllMilestonesByAssignedGoal(goalId: Long)

    fun editMilestone(milestone: Milestone)

    fun deleteMilestone(milestoneId: Long)

    fun addNewWork(milestoneId: Long, description: String)

    fun getAllWorkByAssignedMilestone(milestoneId: Long)

    fun editWork(work: Work)

    fun deleteWork(workId: Long)
}