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
        val works = workRepository.allWorks
        val dateFormat = SimpleDateFormat("EEE", Locale.US)
        val dataPoints = mutableListOf<Point>()
        val week = getWeek()

        for (day in week) {
            val achievedWork = works.filter { it.dateAchieved == day }
            dataPoints.add(Point(dateFormat.format(day), achievedWork.size.toFloat()))
        }

        mMainThread.post(Runnable { mCallback.onWeeklyLineGraphRetrieved(dataPoints) })
    }

    fun getWeek(): List<Date> {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        today.firstDayOfWeek = Calendar.MONDAY

        val calendar = Calendar.getInstance()
        calendar.time = today.time
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val week = mutableListOf<Date>()

        for (i in 0..6) {
            week.add(calendar.time)
            if (calendar.time == today.time)
                calendar.add(Calendar.DATE, -7)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return week
    }
}