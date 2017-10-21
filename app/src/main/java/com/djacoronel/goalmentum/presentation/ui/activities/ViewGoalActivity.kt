package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
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
import kotlinx.android.synthetic.main.input_dialog.view.*
import org.jetbrains.anko.alert
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.EditorInfo


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
        title = intent.getStringExtra("extra_goal_desc_key")

        mViewGoalPresenter = ViewGoalPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )

        mAdapter = MilestoneItemAdapter(this)
        milestone_recycler.layoutManager = LinearLayoutManager(this)
        milestone_recycler.adapter = mAdapter

        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(message: String) {
    }

    fun createInputDialogView(): View{
        val view = View.inflate(this, R.layout.input_dialog, null)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        view.input_item_text.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        view.input_item_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                view.add_item_button.performClick()
            false
        }

        return view
    }

    override fun onClickAddMilestone() {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_item_text.hint = "Milestone Description"
        view.add_item_button.setOnClickListener {
            mViewGoalPresenter.addNewMilestone(goalId, view.input_item_text.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInputFromWindow(view.windowToken, 0, 0)
            alert.dismiss()
        }
    }

    override fun onClickEditMilestone(milestone: Milestone) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        view.input_item_text.hint = "Milestone Description"
        view.input_item_text.setText(milestone.description)
        view.add_item_button.setOnClickListener {
            milestone.description = view.input_item_text.text.toString()
            mAdapter.notifyDataSetChanged()
            mViewGoalPresenter.editMilestone(milestone)
            imm.toggleSoftInputFromWindow(view.windowToken, 0, 0)
            alert.dismiss()
        }
    }

    override fun onClickDeleteMilestone(milestoneId: Long) {
        mViewGoalPresenter.deleteMilestone(milestoneId)
    }

    override fun onMilestoneAdded() {
        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }

    override fun showMilestones(milestones: List<Milestone>) {
        mAdapter.showNewMilestones(milestones)
    }

    override fun onExpandMilestone(milestoneId: Long) {
        mViewGoalPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }

    override fun onClickAddWork(milestoneId: Long) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        view.input_item_text.hint = "Work Description"
        view.add_item_button.setOnClickListener {
            mViewGoalPresenter.addNewWork(milestoneId, view.input_item_text.text.toString())
            imm.toggleSoftInputFromWindow(view.windowToken, 0, 0)
            alert.dismiss()
        }
    }

    override fun onWorkAdded(milestoneId: Long) {
        mViewGoalPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }

    override fun showWork(milestoneId: Long, works: List<Work>) {
        mAdapter.showWorksInMilestone(milestoneId, works)
    }

    override fun onClickDeleteWork(workId: Long) {
        mViewGoalPresenter.deleteWork(workId)
    }

    override fun onClickEditWork(work: Work) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        view.input_item_text.hint = "Work Description"
        view.input_item_text.setText(work.description)
        view.add_item_button.setOnClickListener {
            work.description = view.input_item_text.text.toString()
            mAdapter.notifyDataSetChanged()
            mViewGoalPresenter.editWork(work)
            imm.toggleSoftInputFromWindow(view.windowToken, 0, 0)
            alert.dismiss()
        }
    }

    override fun onClickToggleWork(work: Work) {
        work.achieved = !work.achieved
        mViewGoalPresenter.editWork(work)
    }
}
