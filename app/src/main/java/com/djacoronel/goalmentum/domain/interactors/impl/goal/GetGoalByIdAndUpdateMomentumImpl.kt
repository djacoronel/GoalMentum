package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdAndUpdateMomentumInteractor
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.util.DateUtils

/**
 * Created by djacoronel on 10/6/17.
 */

class GetGoalByIdAndUpdateMomentumImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mCallback: GetGoalByIdAndUpdateMomentumInteractor.Callback,
        private val mGoalRepository: GoalRepository,
        private val mAchievedGoalId: Long,
        private val mMomentum: Int
) : AbstractInteractor(threadExecutor, mainThread), EditGoalInteractor {

    override fun run() {
        val goalToUpdate = mGoalRepository.getGoalById(mAchievedGoalId)

        goalToUpdate?.let {

            it.applyDailyMomentumDeductions()
            it.updateMomentum(mMomentum)
            it.momentumDateUpdated = DateUtils.today

            mGoalRepository.update(goalToUpdate)
            mMainThread.post(Runnable { mCallback.onGoalMomentumUpdated(it) })
        }
    }
}