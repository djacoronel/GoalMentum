package com.djacoronel.milestonementum.domain.interactors.impl.milestone

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.milestone.GetAllMilestonesInteractor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */

class GetAllWorkInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mMilestoneRepository: MilestoneRepository,
        private val mCallback: GetAllMilestonesInteractor.Callback
) : AbstractInteractor(threadExecutor, mainThread), GetAllMilestonesInteractor {

    private val mMilestoneComparator = Comparator<Milestone> { lhs, rhs ->
        if (lhs.date!!.before(rhs.date))
            return@Comparator 1

        if (rhs.date!!.before(lhs.date)) -1 else 0
    }

    override fun run() {
        val milestones = mMilestoneRepository.allMilestones
        Collections.sort(milestones, mMilestoneComparator)
        mMainThread.post(Runnable { mCallback.onMilestonesRetrieved(milestones) })
    }
}