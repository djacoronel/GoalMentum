package com.djacoronel.goalmentum.domain.interactors.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetLineGraphDataInteractor
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by djacoronel on 10/31/17.
 */
class GetLineGraphDataInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val workRepository: WorkRepository,
        private val mCallback: GetLineGraphDataInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetLineGraphDataInteractor {
    override fun run() {
        val currentWeek = DateUtils.getCurrentWeek()
        val currentWeekData = createDataPairs(currentWeek)
        val sortedCurrentWeekData = sortDataPairs(currentWeekData)

        val previousWeek = DateUtils.getPreviousWeek()
        val previousWeekData = createDataPairs(previousWeek)
        val sortedPreviousWeekData = sortDataPairs(previousWeekData)

        mMainThread.post(Runnable { mCallback.onLineGraphDataRetrieved(sortedCurrentWeekData, sortedPreviousWeekData) })
    }

    fun createDataPairs(week: List<Date>): List<Pair<String, Int>> {
        val dateFormat = SimpleDateFormat("EEE", Locale.US)
        val dataPairs = mutableListOf<Pair<String, Int>>()
        val works = workRepository.allWorks

        for (day in week) {
            val achievedWork = works.filter { it.dateAchieved == day }
            dataPairs.add(Pair(dateFormat.format(day), achievedWork.size))
        }

        return dataPairs
    }

    fun sortDataPairs(dataPairs: List<Pair<String, Int>>): List<Pair<String, Int>> {
        val sortedDataPairs = mutableListOf<Pair<String, Int>>()
        sortedDataPairs.add(dataPairs.find { it.first == "Mon" }!!)
        sortedDataPairs.add(dataPairs.find { it.first == "Tue" }!!)
        sortedDataPairs.add(dataPairs.find { it.first == "Wed" }!!)
        sortedDataPairs.add(dataPairs.find { it.first == "Thu" }!!)
        sortedDataPairs.add(dataPairs.find { it.first == "Fri" }!!)
        sortedDataPairs.add(dataPairs.find { it.first == "Sat" }!!)
        sortedDataPairs.add(dataPairs.find { it.first == "Sun" }!!)

        return sortedDataPairs
    }
}