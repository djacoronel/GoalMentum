package com.djacoronel.goalmentum.storage

import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.storage.converter.GoalStorageModelConverter
import com.djacoronel.goalmentum.storage.model.Goal_Table
import com.raizlabs.android.dbflow.sql.language.SQLite


/**
 * Created by djacoronel on 10/6/17.
 */
class GoalRepositoryImpl : GoalRepository {

    override val allGoals: List<Goal>
        get() {

            val goals = SQLite
                    .select()
                    .from(com.djacoronel.goalmentum.storage.model.Goal::class.java)
                    .queryList()

            return GoalStorageModelConverter.convertListToDomainModel(goals)
        }

    override val allAchievedGoals: List<Goal>
        get() {

            val costs = SQLite
                    .select()
                    .from(com.djacoronel.goalmentum.storage.model.Goal::class.java)
                    .where(Goal_Table.achieved.eq(true))
                    .queryList()

            return GoalStorageModelConverter.convertListToDomainModel(costs)
        }


    override fun insert(goal: Goal) {
        val dbItem = GoalStorageModelConverter.convertToStorageModel(goal)
        dbItem.insert()
    }

    override fun update(goal: Goal) {
        val dbItem = GoalStorageModelConverter.convertToStorageModel(goal)
        dbItem.update()
    }

    override fun delete(goal: Goal) {
        val dbItem = GoalStorageModelConverter.convertToStorageModel(goal)
        dbItem.delete()
    }

    override fun getGoalById(id: Long): Goal {
        val goal = SQLite
                .select()
                .from(com.djacoronel.goalmentum.storage.model.Goal::class.java)
                .where(Goal_Table.id.eq(id))
                .querySingle()

        return GoalStorageModelConverter.convertToDomainModel(goal!!)
    }

    override fun markAchieved(goal: Goal) {
        val storageGoal = GoalStorageModelConverter.convertToStorageModel(goal)
        storageGoal.achieved = true
        storageGoal.update()
    }
}