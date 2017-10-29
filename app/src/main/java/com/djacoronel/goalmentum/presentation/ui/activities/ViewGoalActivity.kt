package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
import android.content.Intent
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

        fab.setOnClickListener { onClickAddMilestone() }

        mAdapter = MilestoneItemAdapter(this)
        milestone_recycler.layoutManager = LinearLayoutManager(this)
        milestone_recycler.adapter = mAdapter

        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
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

    override fun showMilestones(milestones: List<Milestone>, displayedWorks: HashMap<Long, List<Work>>) {
        mAdapter.showMilestones(milestones,displayedWorks)
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
        app_bar.setExpanded(false,true)
        milestone_recycler.smoothScrollToPosition(mAdapter.itemCount)
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
        val intent = Intent(this, AddWorkActivity::class.java)
        intent.putExtra("extra_milestone_id_key", milestoneId)
        startActivity(intent)
    }

    override fun onClickToggleWork(work: Work) {
        mViewGoalPresenter.toggleWork(work)
    }

    override fun onWorkToggled(work: Work) {
        val momentum = if (work.achieved == true) 10 else -10
        mViewGoalPresenter.updateGoalMomentum(goalId, momentum)
        mViewGoalPresenter.toggleMilestoneAchieveStatus(work.assignedMilestone)
        mAdapter.updateWork(work)
    }

    override fun onMilestoneAchieved(milestone: Milestone) {
        mViewGoalPresenter.achieveGoal(goalId)
        mAdapter.updateMilestone(milestone)
    }

    override fun onGoalAchieved(goal: Goal) {
        toast("Goal Achieved! Hooray!")
        finish()
    }

    override fun onResume() {
        super.onResume()
        mViewGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }
}
