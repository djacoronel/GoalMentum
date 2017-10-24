package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AnimationUtils
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
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import org.jetbrains.anko.toast


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
                GoalRepositoryImpl(),
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

    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(view.windowToken, 0, 0)
    }

    fun createInputDialogView(): View {
        val view = View.inflate(this, R.layout.input_dialog, null)
        view.input_item_text.requestFocus()
        view.input_item_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                view.add_item_button.performClick()
            false
        }

        showKeyboard()
        return view
    }

    override fun showMilestones(milestones: List<Milestone>) {
        mAdapter.showMilestones(milestones)
        runLayoutAnimation(milestone_recycler)
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onClickAddMilestone() {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_item_text.hint = "Milestone Description"
        view.add_item_button.setOnClickListener {
            mViewGoalPresenter.addNewMilestone(goalId, view.input_item_text.text.toString())
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onMilestoneAdded(milestone: Milestone) {
        mAdapter.addMilestone(milestone)
    }

    override fun onClickEditMilestone(milestone: Milestone) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_item_text.hint = "Milestone Description"
        view.input_item_text.setText(milestone.description)

        view.add_item_button.setOnClickListener {
            milestone.description = view.input_item_text.text.toString()
            mViewGoalPresenter.updateMilestone(milestone)
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onMilestoneUpdated(milestone: Milestone) {
        mAdapter.updateMilestone(milestone)
    }

    override fun onClickDeleteMilestone(milestoneId: Long) {
        mViewGoalPresenter.deleteMilestone(milestoneId)
    }

    override fun onMilestoneDeleted(milestoneId: Long) {
        mAdapter.deleteMilestone(milestoneId)
    }

    override fun onExpandMilestone(milestoneId: Long) {
        mViewGoalPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }


    override fun showWorks(milestoneId: Long, works: List<Work>) {
        mAdapter.mWorkAdapters[milestoneId]?.showWorks(works)
    }

    override fun onClickAddWork(milestoneId: Long) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_item_text.hint = "Work Description"
        view.add_item_button.setOnClickListener {
            mViewGoalPresenter.addNewWork(milestoneId, view.input_item_text.text.toString())
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onWorkAdded(work: Work) {
        mAdapter.mWorkAdapters[work.assignedMilestone]?.addWork(work)
    }

    override fun onClickEditWork(work: Work) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_item_text.hint = "Work Description"
        view.input_item_text.setText(work.description)
        view.add_item_button.setOnClickListener {
            work.description = view.input_item_text.text.toString()
            mAdapter.notifyDataSetChanged()
            mViewGoalPresenter.updateWork(work)

            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onWorkUpdated(work: Work) {
        mAdapter.mWorkAdapters[work.assignedMilestone]?.updateWork(work)
    }

    override fun onClickDeleteWork(work: Work) {
        mViewGoalPresenter.deleteWork(work)
    }

    override fun onWorkDeleted(work: Work) {
        mAdapter.mWorkAdapters[work.assignedMilestone]?.deleteWork(work)
    }

    override fun onClickToggleWork(work: Work) {
        work.achieved = !work.achieved
        mViewGoalPresenter.updateWork(work)

        val milestone = mAdapter.mMilestones.find { it.id == work.assignedMilestone }
        val works = mAdapter.mWorkAdapters[work.assignedMilestone]?.mWorks

        mViewGoalPresenter.toggleMilestoneAchieveStatus(milestone!!, works!!)
        val momentum = if (work.achieved == true) 10 else -10
        mViewGoalPresenter.updateGoalMomentum(goalId,momentum)
    }


    override fun onAllMilestonesAchieved() {
        mViewGoalPresenter.achieveGoal(goalId)
    }

    override fun onGoalAchieved(goal: Goal) {
        toast("Goal Achieved! Hooray!")
        finish()
    }
}
