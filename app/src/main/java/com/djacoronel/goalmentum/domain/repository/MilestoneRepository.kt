package com.djacoronel.goalmentum.domain.repository

import com.djacoronel.goalmentum.domain.model.Milestone

/**
 * Created by djacoronel on 10/6/17.
 */
interface MilestoneRepository {

    val allMilestones: List<Milestone>

    val allAchievedMilestones: List<Milestone>

    fun insert(milestone: Milestone)

    fun update(milestone: Milestone)

    fun getMilestoneById(id: Long): Milestone?

    fun getMilestonesByAssignedGoal(assignedGoal: Long): List<Milestone>

    fun markAchieved(milestone: Milestone)

    fun delete(milestone: Milestone)
}