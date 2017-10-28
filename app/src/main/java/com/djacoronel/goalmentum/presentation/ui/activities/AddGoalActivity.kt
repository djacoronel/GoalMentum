package com.djacoronel.goalmentum.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.presentation.presenters.AddGoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AddGoalPresenterImpl
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.activity_add_goal.*
import org.jetbrains.anko.toast


class AddGoalActivity : AppCompatActivity(), AddGoalPresenter.View {

    lateinit var mPresenter: AddGoalPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        mPresenter = AddGoalPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl(),
                MilestoneRepositoryImpl()
        )

        fab.setOnClickListener { addGoal() }
        setupDurationPicker()
    }

    fun setupDurationPicker() {
        val numbers = resources.getStringArray(R.array.numbers_array)
        val duration = resources.getStringArray(R.array.day_week_month_array)
        val durationWithS = duration.map { it -> it + "s" }.toTypedArray()

        number_picker.minValue = 0
        number_picker.maxValue = numbers.lastIndex
        number_picker.wrapSelectorWheel = true
        number_picker.displayedValues = numbers

        day_week_month_picker.minValue = 0
        day_week_month_picker.maxValue = duration.lastIndex
        day_week_month_picker.wrapSelectorWheel = true
        day_week_month_picker.displayedValues = duration

        number_picker.setOnValueChangedListener { _, _, newVal ->
            if (newVal == 0)
                day_week_month_picker.displayedValues = duration
            else
                day_week_month_picker.displayedValues = durationWithS
        }
    }

    fun addGoal() {
        mPresenter.addNewGoal(goal_desc_input.text.toString(), getDuration())
    }

    fun getDuration(): String {
        val numberIndex = number_picker.value
        val durationIndex = day_week_month_picker.value

        val numberString = number_picker.displayedValues[numberIndex]
        val durationString = day_week_month_picker.displayedValues[durationIndex]

        return numberString + " " + durationString
    }

    override fun onGoalAdded(goalId: Long) {
        mPresenter.addNewGeneralMilestone(goalId)
        toast("New goal saved!")
        finish()
    }
}
