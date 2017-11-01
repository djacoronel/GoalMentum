package com.djacoronel.goalmentum.domain.interactors.impl

import android.util.Log
import com.db.chart.model.LineSet
import com.db.chart.model.Point
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetWeeklyLineGraphInteractor
import com.djacoronel.goalmentum.domain.repository.WorkRepository
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
        val week = getWeek()
        val dataPoints = createDataPoints(week)
        val sortedDataPoints = sortDataPoints(dataPoints)

        mMainThread.post(Runnable { mCallback.onWeeklyLineGraphRetrieved(sortedDataPoints) })
    }

    fun getWeek(): List<Date> {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val week = mutableListOf<Date>()

        for (i in 0..6) {
            week.add(today.time)
            today.add(Calendar.DATE, -1)
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