package com.djacoronel.goalmentum.domain.interactors.impl.goal

import com.djacoronel.goalmentum.domain.executor.Executor
import com.djacoronel.goalmentum.domain.executor.MainThread
import com.djacoronel.goalmentum.domain.interactors.base.goal.EditGoalInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetAllGoalsInteractor
import com.djacoronel.goalmentum.domain.interactors.base.goal.GetGoalByIdInteractor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.domain.repository.GoalRepository
import com.djacoronel.goalmentum.domain.repository.MilestoneRepository
import com.djacoronel.goalmentum.domain.repository.WorkRepository
import com.djacoronel.goalmentum.threading.TestMainThread
import com.djacoronel.goalmentum.util.DateUtils
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by djacoronel on 11/26/17.
 */
class GetGoalByIdInteractorImplTest {

    private lateinit var mainThread: MainThread
    @Mock
    private lateinit var executor: Executor
    @Mock
    private lateinit var goalRepository: GoalRepository
    @Mock
    private lateinit var milestoneRepository: MilestoneRepository
    @Mock
    private lateinit var workRepository: WorkRepository
    @Mock
    private lateinit var callback: GetGoalByIdInteractor.Callback

    private val goalCaptor = argumentCaptor<Goal>()

    val testGoal = Goal(
            0,
            0,
            "TestGoal",
            DateUtils.today,
            "TestDuration",
            false,
            0,
            DateUtils.today
    )

    val testMilestone = Milestone(
            0,
            0,
            0,
            "TestMilestone",
            DateUtils.today,
            false
    )

    val testWork = Work(
            0,
            0,
            0,
            "TestMilestone",
            DateUtils.today,
            false,
            DateUtils.today
    )


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mainThread = TestMainThread()
    }

    @Test
    fun testGoalRetrieval(){
        Mockito.`when`(goalRepository.getGoalById(testGoal.id)).thenReturn(testGoal)
        Mockito.`when`(milestoneRepository.getMilestonesByAssignedGoal(testGoal.id)).thenReturn(listOf(testMilestone))
        Mockito.`when`(workRepository.getWorksByAssignedMilestone(testMilestone.id)).thenReturn(listOf(testWork))

        val interactor = GetGoalByIdInteractorImpl(
                executor,
                mainThread,
                goalRepository,
                milestoneRepository,
                workRepository,
                callback,
                testGoal.id
        )
        interactor.run()

        verify(callback).onGoalRetrieved(goalCaptor.capture())
        val goal = goalCaptor.firstValue

        assertThat(goal.activeWork, CoreMatchers.`is`(1))
        assertThat(goal.achievedWork, CoreMatchers.`is`(0))
        assertThat(goal.achievedWorkToday, CoreMatchers.`is`(0))
        assertThat(goal.achievedMilestone, CoreMatchers.`is`(0))
    }
}