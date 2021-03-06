package com.djacoronel.goalmentum.presentation.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.domain.model.Milestone
import com.djacoronel.goalmentum.domain.model.Work
import com.djacoronel.goalmentum.presentation.presenters.GoalPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.GoalPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.adapters.MilestoneItemAdapter
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import com.djacoronel.goalmentum.util.ProgressBarAnimation
import com.djacoronel.goalmentum.util.TouchHelper
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.achieved_bar.*
import kotlinx.android.synthetic.main.activity_goal.*
import kotlinx.android.synthetic.main.input_dialog.view.*
import kotlinx.android.synthetic.main.momentum_bar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import javax.inject.Inject


class GoalActivity : AppCompatActivity(), GoalPresenter.View {

    @Inject lateinit var mGoalPresenter: GoalPresenter
    private lateinit var mAdapter: MilestoneItemAdapter
    private lateinit var goal: Goal
    private var goalId: Long = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_view_goal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit -> {
                onClickEditGoal(goal)
                true
            }
            R.id.menu_delete -> {
                onClickDeleteGoal(goal)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)
        setSupportActionBar(toolbar)
        init()
    }

    private fun init() {
        goalId = intent.getLongExtra("extra_goal_id_key", -1)

        mGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
        mGoalPresenter.getGoalById(goalId)

        fab.setOnClickListener { onClickAddMilestone() }

        setupMilestoneRecycler()
        setupProgressBarsViewPager()
    }

    fun setupMilestoneRecycler() {
        mAdapter = MilestoneItemAdapter(this)
        milestone_recycler.layoutManager = LinearLayoutManager(this)
        milestone_recycler.adapter = mAdapter

        val callback = TouchHelper(mAdapter)
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(milestone_recycler)
    }

    fun setupProgressBarsViewPager() {
        viewpager.adapter = object : PagerAdapter() {
            internal var layouts = intArrayOf(R.layout.momentum_bar, R.layout.achieved_bar)
            override fun getCount(): Int = layouts.size
            override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val inflater = LayoutInflater.from(this@GoalActivity)
                val layout = inflater.inflate(layouts[position], container, false) as ViewGroup
                container.addView(layout)
                return layout
            }
        }
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
        view.input_work_edittext.requestFocus()
        view.input_work_edittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                view.add_task_button.performClick()
            false
        }

        showKeyboard()
        return view
    }

    override fun onGoalRetrieved(goal: Goal) {
        this.goal = goal
        collapsing_toolbar.title = goal.description
        setGoalProgress(goal)

        if (goal.achieved == true) {
            toast("Goal achieved! Hooray!")
            finish()
        }
    }

    override fun onClickEditGoal(goal: Goal) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_work_edittext.hint = "Goal Description"
        view.input_work_edittext.setText(goal.description)

        view.add_task_button.setOnClickListener {
            goal.description = view.input_work_edittext.text.toString()
            mGoalPresenter.updateGoal(goal)
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onGoalUpdated(goal: Goal) {
        collapsing_toolbar.title = goal.description
    }

    override fun onClickDeleteGoal(goal: Goal) {
        alert {
            message = "Are you sure you want to delete this goal?"
            yesButton { mGoalPresenter.deleteGoal(goal) }
            noButton {  }
        }.show()
    }

    override fun onGoalDeleted(goal: Goal) {
        toast("Goal Deleted")
        finish()
    }

    fun setGoalProgress(goal: Goal) {
        setMomentumProgress(goal.getMomentumWithDeduction())
        setAchievedProgress(goal.activeWork, goal.achievedWork)
    }

    fun setMomentumProgress(momentum: Int) {
        progress_text_momentum?.let {
            val progressTextMomentum = "Momentum $momentum%"
            it.text = progressTextMomentum
        }

        progress_bar_momentum?.let {
            val momentumProgressAnimation = ProgressBarAnimation(it, 2000)
            momentumProgressAnimation.setProgress(momentum)
        }
    }

    fun setAchievedProgress(activeWork: Int, achievedWork: Int) {
        val totalWorks = activeWork + achievedWork
        val progress = if (totalWorks == 0) 0 else ((achievedWork / totalWorks.toFloat()) * 100).toInt()

        progress_text_achieved?.let {
            val progressTextAchieved = "Achieved $achievedWork/$totalWorks"
            it.text = progressTextAchieved
        }

        progress_bar_achieved?.let {
            val achievedProgressAnimation = ProgressBarAnimation(progress_bar_achieved, 2000)
            achievedProgressAnimation.setProgress(progress)
        }
    }

    override fun showMilestones(milestones: List<Milestone>, displayedWorks: HashMap<Long, List<Work>>) {
        mAdapter.showMilestones(milestones, displayedWorks)
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

        view.input_work_edittext.hint = "Milestone Description"
        view.add_task_button.setOnClickListener {
            mGoalPresenter.addNewMilestone(goalId, view.input_work_edittext.text.toString())
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onMilestoneAdded(milestone: Milestone) {
        mAdapter.addMilestone(milestone)
        app_bar.setExpanded(false, true)
        milestone_recycler.smoothScrollToPosition(mAdapter.itemCount)
    }

    override fun onClickEditMilestone(milestone: Milestone) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()

        view.input_work_edittext.hint = "Milestone Description"
        view.input_work_edittext.setText(milestone.description)

        view.add_task_button.setOnClickListener {
            milestone.description = view.input_work_edittext.text.toString()
            mGoalPresenter.updateMilestone(milestone)
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    override fun onMilestoneUpdated(milestone: Milestone) {
        mAdapter.updateMilestone(milestone)
    }

    override fun onClickDeleteMilestone(milestoneId: Long) {
        mGoalPresenter.deleteMilestone(milestoneId)
    }

    override fun onMilestoneDeleted(milestoneId: Long) {
        mAdapter.deleteMilestone(milestoneId)
    }

    override fun onExpandMilestone(milestoneId: Long) {
        val intent = Intent(this, MilestoneActivity::class.java)
        intent.putExtra("extra_milestone_id_key", milestoneId)
        startActivity(intent)
    }

    override fun onClickToggleWork(work: Work) {
        mGoalPresenter.toggleWork(work)
    }

    override fun onWorkToggled(work: Work) {
        mAdapter.updateWork(work)

        mGoalPresenter.getMilestoneById(work.assignedMilestone)
        mGoalPresenter.getGoalById(goalId)
    }

    override fun onMilestoneRetrieved(milestone: Milestone) {
        mAdapter.updateMilestone(milestone)
    }

    override fun onDisplayedWorksAchieved(milestoneId: Long) {
        mGoalPresenter.getAllWorkByAssignedMilestone(milestoneId)
    }

    override fun onNewDisplayedWorksRetrieved(milestoneId: Long, works: List<Work>) {
        mAdapter.updateWorkAdapter(milestoneId, works)
    }

    override fun onSwapMilestonePositions(milestone1: Milestone, milestone2: Milestone) {
        mGoalPresenter.swapMilestonePositions(milestone1, milestone2)
    }

    override fun onResume() {
        super.onResume()
        mGoalPresenter.getGoalById(goalId)
        mGoalPresenter.getAllMilestonesByAssignedGoal(goalId)
    }
}
