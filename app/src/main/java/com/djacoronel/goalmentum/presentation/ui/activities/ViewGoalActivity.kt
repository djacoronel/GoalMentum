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
import kotlinx.android.synthetic.main.input_milestone_item.*
import kotlinx.android.synthetic.main.input_work_item.*
import org.jetbrains.anko.alert

class ViewGoalActivity : AppCompatActivity(), ViewGoalPresenter.View {

    private lateinit var mViewGoalPresenter: ViewGoalPresenter
    private lateinit var mAdapter: MilestoneItemAdapter
    private var goalId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_goal)
        setSupportActionBar(toolbar)

        setupFab()
        init()
    }

    fun setupFab(){
        fab.setOnClickListener {
            val view = View.inflate(this, R.layout.input_work_item, null)
            alert {
                customView = view
                positiveButton("Add Work"){}
                negativeButton("Add Milestone"){}
            }.show()
        }
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
        milestone_recycler.layoutManager.isAutoMeasureEnabled = false
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
        mViewGoalPresenter.addNewWork(milestoneId, input_work_card_text.text.toString())
    }

    override fun onWorkAdded(milestoneId: Long) {
        mViewGoalPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }

    override fun showWork(milestoneId: Long, works: List<Work>) {
        mAdapter.showWorks(milestoneId, works)
    }

    override fun onClickDeleteWork(workId: Long) {
        mViewGoalPresenter.deleteWork(workId)
    }

    override fun onWorkDeleted(work: Work) {
        mViewGoalPresenter.getAllWorkByAssignedMilestone(work.assignedMilestone)
    }
}
