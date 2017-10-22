package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdAndSetAchievedInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */

class GetGoalByIdAndSetAchievedInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mCallback: GetGoalByIdAndSetAchievedInteractor.Callback,
        private val mGoalRepository: GoalRepository,
        private val mAchievedGoalId: Long
) : AbstractInteractor(threadExecutor, mainThread), EditGoalInteractor {

    override fun run() {
        val goalToAchieve = mGoalRepository.getGoalById(mAchievedGoalId)

        goalToAchieve?.let {
            it.achieved = true
            mGoalRepository.update(goalToAchieve)
            mMainThread.post(Runnable { mCallback.onGoalAchieved(it) })
        }
    }
}