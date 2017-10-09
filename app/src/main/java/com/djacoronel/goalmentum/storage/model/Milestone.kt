package com.djacoronel.goalmentum.storage.model

import com.djacoronel.goalmentum.storage.database.MilestoneDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */
@Table(database = MilestoneDatabase::class)
class Milestone : BaseModel {

    @PrimaryKey
    var id: Long = 0

    @Column
    var assignedGoal: Long = 0

    @Column
    var description: String? = null

    @Column
    var date: Date? = null

    @Column(getterName = "getAchieved")
    var achieved: Boolean = false

    constructor() {}

    constructor(assignedGoal: Long, description: String, date: Date) {
        this.id = Date().time
        this.assignedGoal = assignedGoal
        this.description = description
        this.date = date
        this.achieved = false
    }

    override fun toString(): String {
        return "Milestone{" +
                "id=" + id +
                ", assignedGoal=" + assignedGoal +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}'
    }
}
