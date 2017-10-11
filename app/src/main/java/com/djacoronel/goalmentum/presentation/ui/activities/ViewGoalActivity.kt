package com.djacoronel.goalmentum.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.ViewGoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.ViewGoalPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.adapters.MilestoneItemAdapter
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.activity_view_goal.*
import kotlinx.android.synthetic.main.input_milestone_dialog.view.*
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
        goalId = intent.getLongExtra("extra_goal_id_key", -1)
        title =  intent.getStringExtra("extra_goal_desc_key")

        mViewGoalPresenter = ViewGoalPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )

        // setup recycler view adapter
        mAdapter = MilestoneItemAdapter(this)

        // setup recycler view
        milestone_recycler.layoutManager = LinearLayoutManager(this)
        //milestone_recycler.layoutManager.isAutoMeasureEnabled = false
        milestone_recycler.adapter = mAdapter

        // get milestones
        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(message: String) {
    }


    override fun onClickAddMilestone() {
        val view = View.inflate(this, R.layout.input_milestone_dialog, null)
        val alert = alert { customView = view }.show()

        view.add_milestone_button.setOnClickListener {
            mViewGoalPresenter.addNewMilestone(goalId,view.input_milestone_card_text.text.toString())
            alert.dismiss()
        }
    }

    override fun onClickEditMilestone(milestoneId: Long) {

    }

    override fun onClickDeleteMilestone(milestoneId: Long) {
        mViewGoalPresenter.deleteMilestone(milestoneId)
    }

    override fun onMilestoneAdded() {
        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }

    override fun onMilestoneDeleted(milestone: Milestone) {
        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }

    override fun showMilestones(milestones: List<Milestone>) {
        mAdapter.addNewMilestones(milestones)
    }

    override fun onExpandMilestone(milestoneId: Long) {
        mViewGoalPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }

    override fun onClickAddWork(milestoneId: Long) {
        val view = View.inflate(this, R.layout.input_milestone_dialog, null)
        view.input_milestone_card_text.hint = "Work Description"

        val alert = alert { customView = view }.show()

        view.add_milestone_button.setOnClickListener {
            mViewGoalPresenter.addNewWork(milestoneId, view.input_milestone_card_text.text.toString())
            alert.dismiss()
        }

    }

    override fun onWorkAdded(milestoneId: Long) {
        mViewGoalPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }

    override fun showWork(assignedId: Long, works: List<Work>) {
        mAdapter.showWorksInMilestone(assignedId, works)
    }

    override fun onClickDeleteWork(workId: Long) {
        mViewGoalPresenter.deleteWork(workId)
    }

    override fun onWorkDeleted(work: Work) {
        mViewGoalPresenter.getAllWorkByAssignedMilestone(work.assignedMilestone)
    }
}
