package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.presentation.presenters.AddGoalPresenter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_goal.*
import org.jetbrains.anko.toast
import javax.inject.Inject


class AddGoalActivity : AppCompatActivity(), AddGoalPresenter.View {

    @Inject lateinit var mPresenter: AddGoalPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)
        showKeyboard()
        setupDurationPicker()
        setupFab()
    }

    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0)
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

    fun setupFab() {
        fab.setOnClickListener {
            mPresenter.addNewGoal(goal_desc_input.text.toString(), getDuration())
        }
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
        hideKeyboard()
        finish()
        toast("New goal saved!")
    }
}
