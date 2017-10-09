package com.djacoronel.goalmentum.storage.model

import com.djacoronel.goalmentum.storage.database.WorkDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */
@Table(database = WorkDatabase::class)
class Work : BaseModel {

    @PrimaryKey
    var id: Long = 0

    @Column
    var assignedMilestone: Long = 0

    @Column
    var description: String? = null

    @Column
    var date: Date? = null

    @Column(getterName = "getAchieved")
    var achieved: Boolean = false

    constructor() {}

    constructor(assignedMilestone: Long, description: String, date: Date) {
        this.id = Date().time
        this.assignedMilestone = assignedMilestone
        this.description = description
        this.date = date
        this.achieved = false
    }

    override fun toString(): String {
        return "Work{" +
                "id=" + id +
                ", assignedMilestone=" + assignedMilestone +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}'
    }
}
