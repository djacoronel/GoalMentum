package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.DeleteGoalInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.djacoronel.goalmentum.threading.TestMainThread
import com.djacoronel.goalmentum.util.DateUtils
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by djacoronel on 11/18/17.
 */
class DeleteGoalInteractorImplTest {

    private lateinit var mainThread: MainThread
    @Mock
    private lateinit var executor: Executor
    @Mock
    private lateinit var goalRepository: GoalRepository
    @Mock
    private lateinit var callback: DeleteGoalInteractor.Callback

    private val goalIdCaptor = argumentCaptor<Long>()

    private val goal = Goal(
            1,
            1,
            "TestGoal",
            DateUtils.today,
            "TestDuration",
            false,
            0,
            DateUtils.today)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainThread = TestMainThread()
    }

    @Test
    fun testGoalDeleted(){
        val interactor = DeleteGoalInteractorImpl(
                executor,
                mainThread,
                goalRepository,
                callback,
                goal
        )
        interactor.run()

        verify(goalRepository).delete()

    }
}