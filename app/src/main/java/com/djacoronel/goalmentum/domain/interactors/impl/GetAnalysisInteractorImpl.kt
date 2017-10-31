package com.djacoronel.goalmentum.domain.interactors.impl

import android.util.Log
import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetAnalysisInteractor
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.util.DateUtils
import java.util.*


/**
 * Created by djacoronel on 10/31/17.
 */
class GetAnalysisInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val goalRepository: GoalRepository,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository,
        private val mCallback: GetAnalysisInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetAnalysisInteractor {
    override fun run() {
        val goals = goalRepository.allGoals.filter { it.achieved == true }.size
        val milestones = milestoneRepository.allMilestones.filter { it.achieved == true }.size
        val works = workRepository.allWorks.filter { it.achieved == true }.size


        val data = mutableListOf<Int>()

        with(data) {
            add(getAverageWorkPerDay())
            add(getAverageWorkPerWeek())
            add(works)
            add(milestones)
            add(goals)
            add(getMostProductiveDay())
        }

        mMainThread.post(Runnable { mCallback.onAnalysisRetrieved(data) })
    }

    fun getAverageWorkPerDay(): Int {
        val works = workRepository.allWorks
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today

        var sum = 0

        for (i in 0..6) {
            sum += works.filter { it.date == calendar.time && it.achieved == true }.size
            calendar.add(Calendar.DATE, -1)
        }

        Log.i(sum.toString(),"jlj;dgjks")
        return (sum / 7.0).toInt()
    }

    fun getAverageWorkPerWeek(): Int {
        val works = workRepository.allWorks
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        var sum = 0

        for (i in 0..27) {
            sum += works.filter { it.date == calendar.time && it.achieved == true }.size
            calendar.add(Calendar.DAY_OF_WEEK, -1)
        }

        return sum / 4
    }

    fun getMostProductiveDay(): Int {
        val works = workRepository.allWorks
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today

        val sums = arrayOf(0, 0, 0, 0, 0, 0, 0)

        for (i in 0..27) {
            sums[calendar.get(Calendar.DAY_OF_WEEK)-1] += works.filter { it.date == calendar.time && it.achieved == true }.size
            calendar.add(Calendar.DATE, -1)
        }

        Log.i("PRODUCTIVE:",sums.toList().toString())

        return sums.indexOf(sums.max()!!)
    }
}