package com.djacoronel.goalmentum.domain.interactors.impl

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.GetAnalysisDataInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.util.DateUtils
import java.util.*


/**
 * Created by djacoronel on 10/31/17.
 */
class GetAnalysisDataInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val goalRepository: GoalRepository,
        private val milestoneRepository: MilestoneRepository,
        private val workRepository: WorkRepository,
        private val mCallback: GetAnalysisDataInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetAnalysisDataInteractor {
    override fun run() {
        val works = workRepository.allWorks

        val achievedGoals = goalRepository.allGoals.filter { it.achieved == true }.size
        val achievedMilestones = milestoneRepository.allMilestones.filter { it.achieved == true }.size
        val achievedWorks = works.filter { it.achieved == true }.size

        val data = mutableListOf<Int>()

        with(data) {
            add(getAverageWorkPerDay(works))
            add(getAverageWorkPerWeek(works))
            add(achievedWorks)
            add(achievedMilestones)
            add(achievedGoals)
            add(getMostProductiveDay(works))
        }

        mMainThread.post(Runnable { mCallback.onAnalysisDataRetrieved(data) })
    }

    fun getAverageWorkPerDay(works: List<Work>): Int {
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today

        val sumPerDay = mutableListOf<Int>()

        for (i in 0..6) {
            sumPerDay.add(works.filter { it.dateAchieved == calendar.time }.size)
            calendar.add(Calendar.DATE, -1)
        }

        val numOfDaysWithWorkDone = sumPerDay.filter { it != 0 }.size

        return if (numOfDaysWithWorkDone == 0) 0
        else sumPerDay.sum() / numOfDaysWithWorkDone
    }

    fun getAverageWorkPerWeek(works: List<Work>): Int {
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val sumPerWeek = mutableListOf<Int>()

        for (i in 1..4) {
            var sum = 0
            for (j in 1..7) {
                sum += works.filter { it.dateAchieved == calendar.time }.size
                calendar.add(Calendar.DATE, -1)
            }
            sumPerWeek.add(sum)
        }

        val numberOfWeekWithWorkDone = sumPerWeek.filter { it != 0 }.size

        return if (numberOfWeekWithWorkDone == 0) 0
        else sumPerWeek.sum() / numberOfWeekWithWorkDone
    }

    fun getMostProductiveDay(works: List<Work>): Int {
        val calendar = Calendar.getInstance()
        calendar.time = DateUtils.today

        val sumPerDay = arrayOf(0, 0, 0, 0, 0, 0, 0)

        for (i in 0..27) {
            val dayOfWeek = DateUtils.convertValueToMondayFirst(calendar.get(Calendar.DAY_OF_WEEK))
            sumPerDay[dayOfWeek - 1] += works.filter { it.dateAchieved == calendar.time }.size
            calendar.add(Calendar.DATE, -1)
        }

        return sumPerDay.indexOf(sumPerDay.max()!!) + 1
    }
}