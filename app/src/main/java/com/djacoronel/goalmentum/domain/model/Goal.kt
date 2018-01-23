package com.djacoronel.goalmentum.domain.model

import com.djacoronel.goalmentum.util.DateUtils
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by djacoronel on 10/6/17.
 */
class Goal {
    var id: Long = 0
        private set
    var positionInList: Long = 0
    var description: String? = null
    var dateCreated: Date? = null
    var duration: String? = null
    var achieved: Boolean = false
    var momentum: Int = 0
    var momentumDateUpdated: Date? = null

    var activeWork: Int = 0
    var achievedWork: Int = 0
    var achievedWorkToday: Int = 0
    var achievedMilestone: Int = 0

    constructor(description: String, duration: String) {
        val currentDate = DateUtils.today
        this.id = Date().time
        this.positionInList = id
        this.description = description
        this.dateCreated = currentDate
        this.duration = duration
        this.achieved = false
        this.momentum = 0
        this.momentumDateUpdated = currentDate
    }

    constructor(id: Long, positionInList: Long, description: String, date: Date, duration: String, achieved: Boolean, momentum: Int, momentumDateUpdated: Date) {
        this.id = id
        this.positionInList = positionInList
        this.description = description
        this.dateCreated = date
        this.duration = duration
        this.achieved = achieved
        this.momentum = momentum
        this.momentumDateUpdated = momentumDateUpdated
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val goal = other as Goal?

        return id == goal!!.id
    }

    override fun hashCode(): Int {
        return (id xor id.ushr(32)).toInt()
    }

    override fun toString(): String {
        return "Goal{" +
                "id=" + id +
                ", description='" + description + "'" +
                ", dateCreated=" + dateCreated +
                ", duration=" + duration +
                ", achieved=" + achieved +
                ", momentum=" + momentum +
                '}'
    }

    fun updateMomentum(momentumAdjustment: Int) {
        applyDailyMomentumDeductions()
        momentum += momentumAdjustment
        adjustMomentumToLimits()
        momentumDateUpdated = DateUtils.today
    }

    fun getMomentumWithDeduction(): Int {
        applyDailyMomentumDeductions()
        return momentum
    }

    private fun applyDailyMomentumDeductions() {
        val currentDate = Date()
        val elapsedDays = getDifferenceDays(momentumDateUpdated!!, currentDate)


        for (i in 1..elapsedDays.toInt())
            momentum += -10 * i
        adjustMomentumToLimits()
    }

    private fun adjustMomentumToLimits() {
        if (momentum < 0) momentum = 0
        else if (momentum > 100) momentum = 100
    }

    fun getStringRemainingDays(): String {
        val remainingDays = getRemainingDays()
        return when {
            remainingDays.toInt() > 1 -> "($remainingDays days remaining)"
            remainingDays.toInt() == 1 -> "($remainingDays day remaining)"
            remainingDays.toInt() == 0 -> "(DUE!)"
            remainingDays.toInt() == -1 -> "(${-remainingDays} day overdue)"
            remainingDays.toInt() < 0 -> "(${-remainingDays} days overdue)"
            else -> "Invalid remaining days value"
        }
    }

    private fun getRemainingDays(): Long {
        val currentDate = Date()
        val totalDays = getDifferenceDays(dateCreated!!, getEndDate())
        val elapsedDays = getDifferenceDays(dateCreated!!, currentDate)
        return totalDays - elapsedDays
    }

    private fun getDifferenceDays(d1: Date, d2: Date): Long {
        val diff = d2.time - d1.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    private fun getEndDate(): Date {
        val cal = Calendar.getInstance()
        val numberOfUnits = duration!!.substringBefore(" ", "0").toInt()
        cal.time = dateCreated

        when {
            "day" in duration!! -> cal.add(Calendar.DATE, numberOfUnits)
            "week" in duration!! -> cal.add(Calendar.DATE, 7 * numberOfUnits)
            "month" in duration!! -> cal.add(Calendar.MONTH, numberOfUnits)
            "year" in duration!! -> cal.add(Calendar.YEAR, numberOfUnits)
        }

        return cal.time
    }
}