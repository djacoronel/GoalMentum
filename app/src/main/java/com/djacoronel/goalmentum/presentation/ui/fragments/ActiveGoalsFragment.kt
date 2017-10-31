package com.djacoronel.goalmentum.presentation.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.presentation.presenters.MainPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.MainPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.activities.AddGoalActivity
import com.djacoronel.goalmentum.presentation.ui.activities.GoalActivity
import com.djacoronel.goalmentum.presentation.ui.adapters.GoalItemAdapter
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.fragment_active_goals.view.*
import kotlinx.android.synthetic.main.input_dialog.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.support.v4.alert


/**
 * Created by djacoronel on 10/7/17.
 */
class ActiveGoalsFragment : Fragment(), MainPresenter.View {

    private lateinit var mMainPresenter: MainPresenter
    private lateinit var mAdapter: GoalItemAdapter

    fun newInstance(): ActiveGoalsFragment {
        return ActiveGoalsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_active_goals, container, false)
        init(view)

        return view
    }

    private fun init(view: View) {
        // setup recycler view adapter
        mAdapter = GoalItemAdapter(this)

        // setup recycler view
        view.goal_recycler.layoutManager = LinearLayoutManager(activity)
        view.goal_recycler.layoutManager.isAutoMeasureEnabled = false
        view.goal_recycler.adapter = mAdapter

        // instantiate the presenter
        mMainPresenter = MainPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl(),
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )
    }

    override fun showGoals(goals: List<Goal>) {
        mAdapter.showGoals(goals)
        view?.let { runLayoutAnimation(it.goal_recycler) }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onClickViewGoal(goal: Goal) {
        val intent = Intent(context, GoalActivity::class.java)
        intent.putExtra(EXTRA_GOAL_ID, goal.id)
        intent.putExtra(EXTRA_GOAL_DESC, goal.description)
        startActivity(intent)
    }

    override fun onClickAddGoal() {
        val intent = Intent(context, AddGoalActivity::class.java)
        startActivityForResult(intent, ADD_GOAL_REQUEST)
    }

    override fun onGoalAdded(goal: Goal) {
    }

    override fun onClickEditGoal(goal: Goal) {
        val view = createInputDialogView()
        val alert = alert { customView = view }.show()
        showKeyboard()

        view.input_item_text.hint = "Goal Description"
        view.input_item_text.setText(goal.description)

        view.add_task_button.setOnClickListener {
            goal.description = view.input_item_text.text.toString()
            mMainPresenter.updateGoal(goal)
            hideKeyboard(view)
            alert.dismiss()
        }
    }

    fun showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboard(view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(view.windowToken, 0, 0)
    }

    fun createInputDialogView(): View {
        val view = View.inflate(context, R.layout.input_dialog, null)
        view.input_item_text.requestFocus()
        view.input_item_text.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                view.add_task_button.performClick()
            false
        }
        return view
    }

    override fun onGoalUpdated(goal: Goal) {
        mAdapter.updateGoal(goal)
    }

    override fun onClickDeleteGoal(goal: Goal) {
        mMainPresenter.deleteGoal(goal)
    }

    override fun onGoalDeleted(goal: Goal) {
        mAdapter.deleteGoal(goal)
    }

    fun editGoal(goalId: Long) {
        val intent = Intent(context, AddGoalActivity::class.java)
        intent.putExtra(EXTRA_GOAL_ID, goalId)
        startActivityForResult(intent, EDIT_GOAL_REQUEST)
    }

    companion object {
        const val EXTRA_GOAL_ID = "extra_goal_id_key"
        const val EXTRA_GOAL_DESC = "extra_goal_desc_key"
        const val EDIT_GOAL_REQUEST = 1
        const val ADD_GOAL_REQUEST = 0
    }

    override fun onResume() {
        super.onResume()
        mMainPresenter.getAllGoals()
    }
}