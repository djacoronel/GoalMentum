package com.djacoronel.goalmentum.domain.repository

import com.djacoronel.goalmentum.domain.model.Milestone

/**
 * Created by djacoronel on 10/6/17.
 */
interface MilestoneRepository {

    val allMilestones: List<Milestone>

    val allAchievedMilestones: List<Milestone>

    fun insert(Milestone: Milestone)

    fun update(Milestone: Milestone)

    fun getMilestoneById(id: Long): Milestone?

    fun getMilestonesByAssignedGoal(assignedGoal: Long): List<Milestone>

    fun markAchieved(Milestone: Milestone)

    fun delete(Milestone: Milestone)
}