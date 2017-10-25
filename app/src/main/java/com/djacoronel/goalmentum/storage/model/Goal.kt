package com.djacoronel.goalmentum.storage.model

import com.djacoronel.goalmentum.storage.database.GoalDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import java.util.*


/**
 * Created by djacoronel on 10/6/17.
 */
@Table(database = GoalDatabase::class)
class Goal : BaseModel {

    @PrimaryKey
    var id: Long = 0

    @Column
    var description: String? = null

    @Column
    var date: Date? = null

    @Column
    var duration: String? = null

    @Column(getterName = "getAchieved")
    var achieved: Boolean = false

    @Column
    var momentum: Int = 0

    @Column
    var momentumDateUpdated: Date? = null

    constructor()

    constructor(id: Long, description: String, date: Date, duration: String, achieved: Boolean, momentum: Int, momentumDateUpdated: Date) {
        this.id = id
        this.description = description
        this.date = date
        this.duration = duration
        this.achieved = achieved
        this.momentum = momentum
        this.momentumDateUpdated = momentumDateUpdated
    }

    override fun toString(): String {
        return "Goal{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", dateCreated=" + date +
                ", duration=" + duration +
                ", achieved=" + achieved +
                ", momentum=" + momentum +
                '}'
    }
}
