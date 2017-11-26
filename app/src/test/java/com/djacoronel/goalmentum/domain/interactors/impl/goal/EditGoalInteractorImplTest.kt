package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.threading.TestMainThread
import com.djacoronel.goalmentum.util.DateUtils
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * Created by djacoronel on 11/26/17.
 */

class EditGoalInteractorImplTest {

    private lateinit var mainThread: MainThread
    @Mock
    private lateinit var executor: Executor
    @Mock
    private lateinit var goalRepository: GoalRepository
    @Mock
    private lateinit var callback: EditGoalInteractor.Callback

    private val goal = Goal(
            1,
            1,
            "TestGoal",
            DateUtils.today,
            "TestDuration",
            false,
            0,
            DateUtils.today)

    private val updatedGoal = Goal(
            1,
            1,
            "UpdatedGoal",
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
    fun testGoalUpdated() {
        `when`(goalRepository.getGoalById(updatedGoal.id)).thenReturn(goal)

        val interactor = EditGoalInteractorImpl(
                executor,
                mainThread,
                goalRepository,
                callback,
                updatedGoal
        )
        interactor.run()

        verify(goalRepository).update(updatedGoal)
        verify(callback).onGoalUpdated(updatedGoal)
    }

    @Test
    fun testGoalInserted() {
        `when`(goalRepository.getGoalById(goal.id)).thenReturn(null)

        val interactor = EditGoalInteractorImpl(
                executor,
                mainThread,
                goalRepository,
                callback,
                goal
        )
        interactor.run()

        verify(goalRepository).insert(goal)
        verify(callback).onGoalUpdated(goal)
    }
}