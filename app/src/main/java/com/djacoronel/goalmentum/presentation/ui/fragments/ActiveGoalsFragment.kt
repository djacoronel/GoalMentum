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
import org.jetbrains.anko.support.v4.alert
import android.support.v7.widget.helper.ItemTouchHelper
import com.djacoronel.goalmentum.util.TouchHelper


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
        mMainPresenter = MainPresenterImpl(
                ThreadExecutor.instance,
                MainThreadImpl.instance,
                this,
                GoalRepositoryImpl(),
                MilestoneRepositoryImpl(),
                WorkRepositoryImpl()
        )

        setupGoalRecycler(view)

        val headerAnimation = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down)
        view.header.startAnimation(headerAnimation)
    }

    private fun setupGoalRecycler(view: View){
        mAdapter = GoalItemAdapter(this)
        view.goal_recycler.layoutManager = LinearLayoutManager(activity)
        view.goal_recycler.layoutManager.isAutoMeasureEnabled = false
        view.goal_recycler.adapter = mAdapter

        val callback = TouchHelper(mAdapter)
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(view.goal_recycler)
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

    override fun onSwapGoalPositions(goal1: Goal, goal2: Goal) {
        mMainPresenter.swapGoalPositions(goal1, goal2)
    }

    companion object {
        const val EXTRA_GOAL_ID = "extra_goal_id_key"
        const val EXTRA_GOAL_DESC = "extra_goal_desc_key"
        const val ADD_GOAL_REQUEST = 0
    }

    override fun onResume() {
        super.onResume()
        mMainPresenter.getAllGoals()
    }
}