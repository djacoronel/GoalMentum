package com.djacoronel.goalmentum.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.djacoronel.goalmentum.R
import com.djacoronel.goalmentum.domain.executor.impl.ThreadExecutor
import com.djacoronel.goalmentum.domain.model.Goal
import com.djacoronel.goalmentum.presentation.presenters.MainPresenter
import com.djacoronel.goalmentum.presentation.presenters.impl.MainPresenterImpl
import com.djacoronel.goalmentum.presentation.ui.activities.GoalActivity
import com.djacoronel.goalmentum.presentation.ui.adapters.AchievedGoalItemAdapter
import com.djacoronel.goalmentum.storage.GoalRepositoryImpl
import com.djacoronel.goalmentum.storage.MilestoneRepositoryImpl
import com.djacoronel.goalmentum.storage.WorkRepositoryImpl
import com.djacoronel.goalmentum.threading.MainThreadImpl
import kotlinx.android.synthetic.main.fragment_achieved_goals.view.*

/**
 * Created by djacoronel on 10/7/17.
 */
class AchievedGoalsFragment : Fragment(), MainPresenter.View {

    private lateinit var mMainPresenter: MainPresenter
    private lateinit var mAdapter: AchievedGoalItemAdapter

    fun newInstance(): AchievedGoalsFragment {
        return AchievedGoalsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_achieved_goals, container, false)
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

    private fun setupGoalRecycler(view: View) {
        mAdapter = AchievedGoalItemAdapter(this)
        view.goal_recycler.layoutManager = LinearLayoutManager(activity)
        view.goal_recycler.layoutManager.isAutoMeasureEnabled = false
        view.goal_recycler.adapter = mAdapter
    }

    override fun showGoals(goals: List<Goal>) {
        view?.let {
            if (goals.any { it.achieved == true }) {
                mAdapter.showGoals(goals.reversed())
                runLayoutAnimation(it.goal_recycler)
            } else {
                it.goal_recycler.visibility = View.GONE
                it.placeholder_card.visibility = View.VISIBLE

                val placeholderAnimation = AnimationUtils.loadAnimation(it.context, R.anim.item_animation_from_bottom)
                it.placeholder_card.startAnimation(placeholderAnimation)
            }
        }
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
    }

    override fun onGoalAdded(goal: Goal) {
    }

    override fun onClickEditGoal(goal: Goal) {
    }

    override fun onGoalUpdated(goal: Goal) {
    }

    override fun onClickDeleteGoal(goal: Goal) {
    }

    override fun onGoalDeleted(goal: Goal) {
    }

    override fun onSwapGoalPositions(goal1: Goal, goal2: Goal) {
    }

    companion object {
        const val EXTRA_GOAL_ID = "extra_goal_id_key"
        const val EXTRA_GOAL_DESC = "extra_goal_desc_key"
    }

    override fun onResume() {
        super.onResume()
        mMainPresenter.getAllGoals()
    }
}