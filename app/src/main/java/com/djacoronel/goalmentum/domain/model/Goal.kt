package com.djacoronel.goalmentum.domain.model

import com.djacoronel.goalmentum.util.DateUtils
import java.util.*


/**
 * Created by djacoronel on 10/6/17.
 */
class Goal {
    var id: Long = 0
        private set
    var description: String? = null
    var date: Date? = null
    var duration: String? = null
    var achieved: Boolean = false

    constructor(description: String, duration: String) {
        id = Date().time

        this.description = description
        this.date = DateUtils.today
        this.duration = duration
        this.achieved = false
    }

    constructor(id: Long, description: String, date: Date, duration: String, achieved: Boolean) {
        this.id = id
        this.description = description
        this.date = date
        this.duration = duration
        this.achieved = achieved
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val goal = o as Goal?

        return id == goal!!.id
    }

    override fun hashCode(): Int {
        return (id xor id.ushr(32)).toInt()
    }

    override fun toString(): String {
        return "Goal{" +
                "mId=" + id +
                ", mDescription='" + description + '\'' +
                ", mDate=" + date +
                ", mDuration=" + duration +
                '}'
    }
}