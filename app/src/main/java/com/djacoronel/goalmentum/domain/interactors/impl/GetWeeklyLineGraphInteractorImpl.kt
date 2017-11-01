package com.djacoronel.goalmentum.domain.interactors.impl

import android.util.Log
import com.db.chart.model.LineSet
import com.db.chart.model.Point
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetWeeklyLineGraphInteractor
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by djacoronel on 10/31/17.
 */
class GetWeeklyLineGraphInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val workRepository: WorkRepository,
        private val mCallback: GetWeeklyLineGraphInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetWeeklyLineGraphInteractor {
    override fun run() {
        val currentWeek = getCurrentWeek()
        val dataPoints = createDataPoints(currentWeek)
        val sortedDataPoints = sortDataPoints(dataPoints)

        val previousWeek = getPreviousWeek()
        val previousWeekData = createDataPoints(previousWeek)
        val sortedPreviousWeekData = sortDataPoints(previousWeekData)

        mMainThread.post(Runnable { mCallback.onWeeklyLineGraphRetrieved(sortedDataPoints, sortedPreviousWeekData) })
    }

    fun getCurrentWeek(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val week = mutableListOf<Date>()

        for (i in 0..6) {
            week.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }

        return week
    }

    fun getPreviousWeek(): List<Date>{
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.add(Calendar.WEEK_OF_MONTH,-1)

        val week = mutableListOf<Date>()

        for (i in 0..6) {
            week.add(calendar.time)
            calendar.add(Calendar.DATE, 1)
        }

        return week
    }

    fun createDataPoints(week: List<Date>): List<Point> {
        val dateFormat = SimpleDateFormat("EEE", Locale.US)
        val dataPoints = mutableListOf<Point>()
        val works = workRepository.allWorks

        for (day in week) {
            val achievedWork = works.filter { it.dateAchieved == day }
            dataPoints.add(Point(dateFormat.format(day), achievedWork.size.toFloat()))
        }

        return dataPoints
    }

    fun sortDataPoints(dataPoints: List<Point>): List<Point> {
        val sortedDataPoints = mutableListOf<Point>()
        sortedDataPoints.add(dataPoints.find { it.label == "Mon" }!!)
        sortedDataPoints.add(dataPoints.find { it.label == "Tue" }!!)
        sortedDataPoints.add(dataPoints.find { it.label == "Wed" }!!)
        sortedDataPoints.add(dataPoints.find { it.label == "Thu" }!!)
        sortedDataPoints.add(dataPoints.find { it.label == "Fri" }!!)
        sortedDataPoints.add(dataPoints.find { it.label == "Sat" }!!)
        sortedDataPoints.add(dataPoints.find { it.label == "Sun" }!!)

        return sortedDataPoints
    }
}