package com.djacoronel.goalmentum.domain.interactors.impl.work

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.AbstractInteractor
import com.djacoronel.goalmentum.domain.interactors.base.work.GetAllWorksByAssignedMilestoneInteractor
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import java.util.*

/**
 * Created by djacoronel on 10/6/17.
 */

class GetWorksByAssignedMilestoneInteractorImpl(
        threadExecutor: Executor,
        mainThread: MainThread,
        private val mWorkRepository: WorkRepository,
        private val mCallback: GetAllWorksByAssignedMilestoneInteractor.Callback,
        private val mMilestoneId: Long
) : AbstractInteractor(threadExecutor, mainThread), GetAllWorksByAssignedMilestoneInteractor {

    private val mWorkComparator = Comparator<Work> { lhs, rhs ->
        if (lhs.date!!.before(rhs.date))
            return@Comparator 1

        if (rhs.date!!.before(lhs.date)) -1 else 0
    }

    override fun run() {
        val works = mWorkRepository.getWorksByAssignedMilestone(mMilestoneId)
        Collections.sort(works, mWorkComparator)
        mMainThread.post(Runnable { mCallback.onWorksRetrieved(mMilestoneId, works) })
    }
}