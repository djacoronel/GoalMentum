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

    constructor() {}

    constructor(description: String, date: Date, duration: String) {
        this.id = Date().time
        this.description = description
        this.date = date
        this.duration = duration
        this.achieved = false
    }

    override fun toString(): String {
        return "Goal{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                '}'
    }
}
