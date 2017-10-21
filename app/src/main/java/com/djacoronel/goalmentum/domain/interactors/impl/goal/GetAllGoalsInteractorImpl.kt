package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetAllGoalsInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import java.util.*


/**
 * Created by djacoronel on 10/6/17.
 */

class GetAllGoalsInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mGoalRepository: GoalRepository,
        private val mCallback: GetAllGoalsInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetAllGoalsInteractor {

    private val mGoalComparator = Comparator<Goal> { lhs, rhs ->
        if (lhs.date!!.before(rhs.date))
            return@Comparator 1

        if (rhs.date!!.before(lhs.date)) -1 else 0
    }

    override fun run() {
        val goals = mGoalRepository.allGoals
        mMainThread.post(Runnable { mCallback.onGoalsRetrieved(goals) })
    }
}