package com.djacoronel.goalmentum.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.ViewGoalPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.adapters.MilestoneItemAdapter
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.activity_view_goal.*
import kotlinx.android.synthetic.main.input_milestone_item.*
import org.jetbrains.anko.alert

class ViewGoalActivity : AppCompatActivity(), ViewGoalPresenter.View {

    private lateinit var mViewGoalPresenter: ViewGoalPresenter
    private lateinit var mAdapter: MilestoneItemAdapter
    private var goalId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_goal)
        setSupportActionBar(toolbar)

        init()
    }

    private fun init() {
        // instantiate the presenter
        mViewGoalPresenter = ViewGoalPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl(),
                MilestoneRepositoryImpl()
        )

        // get milestones
        goalId = intent.getLongExtra("extra_goal_id_key", -1)
        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)

        // setup recycler view adapter
        mAdapter = MilestoneItemAdapter(this)

        // setup recycler view
        milestone_recycler.layoutManager = LinearLayoutManager(this)
        milestone_recycler.layoutManager.isAutoMeasureEnabled = false
        milestone_recycler.adapter = mAdapter

        // set goal description
        mViewGoalPresenter.getGoalById(goalId)
    }

    override fun setGoalDescription(goalDesc: String) {
        Log.i("TAGGERS:",goalDesc)
        title = goalDesc
    }

    override fun showMilestones(milestones: List<Milestone>) {
        mAdapter.addNewMilestones(milestones)
    }

    override fun showProgress() {
    }

    override fun onClickMilestone(milestoneId: Long) {
    }

    override fun onClickAddMilestone() {
        mViewGoalPresenter.addNewMilestone(goalId, input_milestone_card_text.text.toString())
    }

    override fun onLongClickMilestone(milestoneId: Long) {
        alert {
            title = "Edit or delete milestone?"
            positiveButton("Edit") { }
            negativeButton("Delete") { mViewGoalPresenter.deleteMilestone(milestoneId) }
        }.show()
    }

    override fun hideProgress() {
    }

    override fun onMilestoneAdded() {
        val goalId = intent.getLongExtra("extra_goal_id_key", -1)
        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }

    override fun onMilestoneDeleted(milestone: Milestone) {
        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }

    override fun showError(message: String) {
    }

}
