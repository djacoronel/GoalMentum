package com.djacoronel.goalmentum.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.presentation.presenters.AddGoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.AddGoalPresenterImpl
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
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
                GoalRepositoryImpl()
        )

        fab.setOnClickListener { addGoal() }
    }

    fun addGoal() {
        mPresenter.addNewGoal(goal_desc_input.text.toString(), "duration placeholder")
    }

    override fun onGoalAdded() {
        toast("New goal saved!")
        finish()
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(message: String) {
    }
}
