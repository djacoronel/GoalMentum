package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.AddGoalInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.threading.TestMainThread
import com.nhaarman.mockito_kotlin.argumentCaptor
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.verify

/**
 * Created by djacoronel on 11/6/17.
 */
class AddGoalInteractorImplTest {

    private lateinit var mainThread: MainThread
    @Mock
    private lateinit var executor: Executor
    @Mock
    private lateinit var goalRepository: GoalRepository
    @Mock
    private lateinit var callback: AddGoalInteractor.Callback

    private val goalCaptor = argumentCaptor<Goal>()

    private val goalDescription = "Test goal description"
    private val goalDuration = "1 day"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainThread = TestMainThread()
    }

    @Test
    fun testGoalAdded(){
        val interactor = AddGoalInteractorImpl(
                executor,
                mainThread,
                callback,
                goalRepository,
                goalDescription,
                goalDuration
        )
        interactor.run()

        verify(goalRepository).insert(goalCaptor.capture())
        val insertedGoal = goalCaptor.firstValue
        assertThat(insertedGoal.description, `is`(goalDescription))
        assertThat(insertedGoal.duration, `is`(goalDuration))

        verify(callback).onGoalAdded(insertedGoal.id)
    }
}